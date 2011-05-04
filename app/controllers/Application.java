package controllers;

import com.google.common.collect.Sets;
import models.Liked;
import play.mvc.Controller;

import java.util.HashSet;

import static Utils.Redis.newConnection;

public class Application extends Controller {

   public static void index() {
      render();
   }

   public static void search(String text) {
      // TODO : need Lucene or Solr here
      HashSet likedList = Sets.newHashSet(Liked.findAll());
      Liked.fill(likedList, Security.connectedUser(), newConnection());
      renderJSON(likedList);
   }

   public static void lastAdded(int howMany) {
      HashSet likedList = Sets.newHashSet(Liked.findAll());
      Liked.fill(likedList, Security.connectedUser(), newConnection());
      renderJSON(likedList);
   }

   public static void mostLiked(int howMany) {
      HashSet likedList = Sets.newHashSet(Liked.findAll());
      Liked.fill(likedList, Security.connectedUser(), newConnection());
      renderJSON(likedList);
   }

}