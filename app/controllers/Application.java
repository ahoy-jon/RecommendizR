package controllers;

import static Utils.Redis.newConnection;

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
import services.CrossingBooleanRecommenderBuilder;
import services.CrossingDataModelBuilder;

public class Application extends Controller {

   public static void index() {
      render();
   }

   public static void search(String text) {
      // TODO : need Lucene or Solr here
      renderJSON(Liked.findAll());
   }

   public static void lastAdded(int howMany) {
      renderJSON(Liked.findAll());
   }

   public static void mostLiked(int howMany) {
      renderJSON(Liked.findAll());
   }

}