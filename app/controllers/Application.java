package controllers;

import services.SearchService;
import com.google.common.collect.Sets;
import models.Liked;
import play.Logger;
import play.mvc.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static Utils.Redis.newConnection;
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
      Set<Liked> likedSet = Sets.newHashSet(Liked.<Liked>findAll());
      Liked.fill(likedSet, Security.connectedUser(), newConnection());
      renderJSON(likedSet);
   }

   public static void mostLiked(int howMany) {
      Set<Liked> likedList = Sets.newHashSet(Liked.<Liked>findAll());
      Liked.fill(likedList, Security.connectedUser(), newConnection());
      renderJSON(likedList);
   }

}