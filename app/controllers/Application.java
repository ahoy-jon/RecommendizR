package controllers;

import static Utils.Redis.newConnection;

import play.*;
import play.libs.Crypto;
import play.mvc.*;

import java.util.*;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.BooleanUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import models.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import services.CrossingBooleanRecommenderBuilder;
import services.CrossingDataModelBuilder;

public class Application extends Controller {

   public static void index() {
      render();
   }

   public static void like(Long likedId) {
      User user = Security.connectedUser();
      Jedis jedis = newConnection();
      jedis.hincrBy("l" + likedId, "count", 1);
      jedis.hset("u" + user.id, "like:l" + likedId, String.valueOf(likedId));
      jedis.hset("l" + likedId, "user:u" + user.id, String.valueOf(user.id));
   }

   public static void unlike(Long likedId) {
      User user = Security.connectedUser();
      Jedis jedis = newConnection();
      jedis.hincrBy("l" + likedId, "count", -1);
      jedis.hdel("u" + user.id, "like:l" + likedId);
      jedis.hdel("l" + likedId, "user:u" + user.id);
   }

   public static void addLiked(Liked liked) {
      liked.save();
      like(liked.id);
      render(liked);
   }

   public static boolean isLiked(Long likedId) {
      User user = Security.connectedUser();
      Jedis jedis = newConnection();
      boolean r = null != jedis.hget("u" + user.id, "like:l" + likedId);
      return r;
   }

   public static void search(String text) {
      renderJSON(Liked.findAll());
   }

   public static void lastAdded(int howMany) {
      renderJSON(Liked.findAll());
   }

   public static void mostLiked(int howMany) {
      renderJSON(Liked.findAll());
   }

   public static void recommend(int howMany) throws TasteException {
      User user = Security.connectedUser();
      Jedis jedis = newConnection();
      int limit = 100;
      FastByIDMap<PreferenceArray> usersData = usersData(jedis, null, limit);
      usersData.put(user.id, getPreferences(jedis, limit++, user.id));
      List<RecommendedItem> recommendedItems = recommend(howMany, user, usersData);
      Set<Liked> likedList = new HashSet<Liked>(recommendedItems.size());
      for(RecommendedItem item : recommendedItems) {
         likedList.add(findLiked(item.getItemID()));
      }
      renderJSON(likedList);
   }

   public static List<RecommendedItem> recommend(int howMany, User user, FastByIDMap<PreferenceArray> usersData) throws TasteException {
      RecommenderBuilder recommenderBuilder = new CrossingBooleanRecommenderBuilder();
      DataModel trainingModel = new CrossingDataModelBuilder().buildDataModel(usersData);
      Recommender recommender = recommenderBuilder.buildRecommender(trainingModel);
      return recommender.recommend(user.id, howMany, null);
   }

   private static Liked findLiked(long itemID) {
      return Liked.findById(itemID);
   }

   private static FastByIDMap<PreferenceArray> usersData(Jedis jedis, Category category, int limit) {
      FastByIDMap<PreferenceArray> result = new FastByIDMap<PreferenceArray>();
      Set<String> usersIds = jedis.smembers("users");
      int numUser = 0;
      for (String userId : usersIds) {
         if (numUser > limit) {
            break;
         }
         BooleanUserPreferenceArray preferenceArray = getPreferences(jedis, numUser, Long.valueOf(userId));
         result.put(Long.valueOf(userId), preferenceArray);
         numUser++;
      }

      return result;
   }

   private static BooleanUserPreferenceArray getPreferences(Jedis jedis, int numUser, Long userId) {
      Map<String, String> likedIds = jedis.hgetAll("u" + userId);
      BooleanUserPreferenceArray preferenceArray = new BooleanUserPreferenceArray(likedIds.size());
      preferenceArray.setUserID(numUser, userId);
      int numLiked = 0;
      for (Map.Entry<String, String> likedEntry : likedIds.entrySet()) {
         preferenceArray.setItemID(numLiked++, Long.valueOf(likedEntry.getValue()));
      }
      return preferenceArray;
   }

   public static void handleSignup(User user) {
      if (!validation.valid(user).ok) {
         boolean userAlreadyExists = userAlreadyExists(user);
         render("@signupWithErrors", user, userAlreadyExists);
      } else {
         createUser(user);
      }
   }

   static void createUser(User user) {
      // TODO : send an email.
      boolean userAlreadyExists = userAlreadyExists(user);
      if (userAlreadyExists) {
         render("@signupWithErrors", user, userAlreadyExists);
      }
      user.password = Crypto.passwordHash(user.password);
      user.passwordConfirm = Crypto.passwordHash(user.passwordConfirm);
      user.save();
      Jedis jedis = newConnection();
      jedis.sadd("users", String.valueOf(user.id));
      Security.connect(user, false);
      render(user);
   }

   protected static boolean userAlreadyExists(User user) {
      return (null != Security.findUser(user.username));
   }

}