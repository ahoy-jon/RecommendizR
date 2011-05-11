package controllers;

import models.User;
import play.db.jpa.JPA;
import redis.clients.jedis.Jedis;
import services.SearchService;
import com.google.common.collect.Sets;
import models.Liked;
import play.Logger;
import play.mvc.Controller;

import java.io.IOException;
import java.util.*;

import static Utils.Redis.newConnection;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

import org.apache.lucene.queryParser.ParseException;

import javax.persistence.Query;

public class Application extends Controller {

   public static void index() {
      render();
   }

   public static void home() {
      render();
   }

   public static void liked(Long id) {
      Liked liked = findLiked(id);
      render(liked);
   }

   public static void search(String text) {
      Set<Liked> likedSet = null;
      try {
         List<Liked> likedList = SearchService.search(text);
         if (isEmpty(likedList)) {
            likedSet = Sets.newHashSet();
         } else {
            likedSet = Sets.newHashSet(likedList);
         }
      } catch (IOException e) {
         Logger.error(e, e.getMessage());
         error(e.getMessage());
      } catch (ParseException e) {
         error(e.getMessage());
      }
      Liked.fill(likedSet, Security.connectedUser(), newConnection());
      renderJSON(likedSet);
   }

   public static void lastAdded(int howMany) {
      User user = Security.connectedUser();
      Jedis jedis = newConnection();
      Collection<Liked> list = likedList(user, jedis, "recents");
      renderJSON(list);
   }

   static <T extends Collection<Liked>> T removeIgnored(T likedCol, User user, Jedis jedis) {
      if (user == null || likedCol == null) {
         return likedCol;
      } else {
         Map<String, String> ignoreList = jedis.hgetAll("ignore:u" + user.id);
         for (Iterator<Liked> iter = likedCol.iterator(); iter.hasNext();) {
            Liked liked = iter.next();
            if (ignoreList.containsKey("like:l" + liked.id)) {
               iter.remove();
            }
         }
         return likedCol;
      }
   }

   public static void mostLiked(int howMany) {
      User user = Security.connectedUser();
      Jedis jedis = newConnection();
      Collection<Liked> list = likedList(user, jedis, "popular");
      renderJSON(list);
   }

   static Collection<Liked> likedList(User user, Jedis jedis, String listName) {
      Map<String, String> mostPopulars = jedis.hgetAll(listName);
      if (mostPopulars == null || mostPopulars.size() == 0) {
         return new ArrayList<Liked>();
      } else {
         Query query = JPA.em().createQuery("from Liked where id in (:list)");
         List<Long> ids = new ArrayList<Long>();

         for (String s : mostPopulars.keySet()) {
            ids.add(Long.valueOf(s));
         }
         query.setParameter("list", ids);
         Collection<Liked> likedSet = Sets.newHashSet(query.getResultList());
         removeIgnored(likedSet, user, jedis);
         Liked.fill(likedSet, user, jedis);
         return likedSet;
      }
   }

   static Liked findLiked(long itemID) {
      return Liked.findById(itemID);
   }
}