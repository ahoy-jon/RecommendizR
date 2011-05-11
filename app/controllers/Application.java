package controllers;

import models.User;
import redis.clients.jedis.Jedis;
import services.SearchService;
import com.google.common.collect.Sets;
import models.Liked;
import play.Logger;
import play.mvc.Controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static Utils.Redis.newConnection;
import static org.apache.commons.collections.CollectionUtils.EMPTY_COLLECTION;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

import org.apache.lucene.queryParser.ParseException;

public class Application extends Controller {

   public static void index() {
      render();
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
      Set<Liked> likedSet = Sets.newHashSet(Liked.<Liked>findAll());
      removeIgnored(likedSet, user, jedis);
      Liked.fill(likedSet, user, jedis);
      renderJSON(likedSet);
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
      Set<Liked> likedSet = Sets.newHashSet(Liked.<Liked>findAll());
      removeIgnored(likedSet, user, jedis);
      Liked.fill(likedSet, user, jedis);
      renderJSON(likedSet);
   }

}