import java.util.List;

import controllers.Reco;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.BooleanUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.User;
import play.test.*;
import services.CrossingBooleanRecommenderBuilder;
import services.CrossingDataModelBuilder;
import services.CrossingRecommenderBuilder;

public class BasicTest extends UnitTest {

   private static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

   @Test
   public void testOne() throws TasteException {
      RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
      FastByIDMap<PreferenceArray> usersData = usersData();
      DataModel model = new GenericDataModel(usersData);

      double evaluation = evaluator.evaluate(new CrossingRecommenderBuilder(),
              null,
              model,
              1,
              0.5);
      log.info(String.valueOf(evaluation));
   }

   @Test
   public void testTwo() throws TasteException {
      RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
      FastByIDMap<PreferenceArray> usersData = usersData();
      DataModel model = new GenericDataModel(usersData);

      IRStatistics evaluation = evaluator.evaluate(
              new CrossingBooleanRecommenderBuilder(),
              new CrossingDataModelBuilder(),
              model,
              null,
              2,
              Double.NEGATIVE_INFINITY,
              0.5);

      log.info(String.valueOf(evaluation));
   }

   @Test
   public void testRecommendation() throws TasteException {
      int howMany = 4;
      List<RecommendedItem> recommendation = Reco._internalRecommend(howMany, new User(0l), usersData());
      assertTrue(recommendation.size() <= howMany);
      assertEquals(ITEM_9,(Long) recommendation.get(0).getItemID());
      assertEquals(ITEM_7,(Long)  recommendation.get(1).getItemID());
      assertEquals(ITEM_6,(Long)  recommendation.get(2).getItemID());
      assertEquals(ITEM_11,(Long)  recommendation.get(3).getItemID());
   }

     @Test
   public void testNewUserNotInData() throws TasteException {
      int howMany = 4;
      List<RecommendedItem> recommendation = Reco._internalRecommend(howMany, new User(123l), usersData());
      assertTrue(recommendation.size() == howMany);
   }

    @Test
   public void testNewUserInData() throws TasteException {
      int howMany = 4;
      List<RecommendedItem> recommendation = Reco._internalRecommend(howMany, new User(10l), usersData());
      assertTrue(recommendation.size() == howMany);
   }

    @Test
   public void testUserWithOnlyOnePref() throws TasteException {
      int howMany = 4;
      List<RecommendedItem> recommendation = Reco._internalRecommend(howMany, new User(11l), usersData());
      assertTrue(recommendation.size() == howMany);
   }

    @Test
   public void testUserWithOnlyOneUniquePref() throws TasteException {
      int howMany = 4;
      List<RecommendedItem> recommendation = Reco._internalRecommend(howMany, new User(12l), usersData());
      assertTrue(recommendation.size() == howMany);
   }

   static final Long ITEM_1 = 10l;
   static final Long ITEM_2 = 20l;
   static final Long ITEM_3 = 30l;
   static final Long ITEM_4 = 40l;
   static final Long ITEM_5 = 50l;
   static final Long ITEM_6 = 60l;
   static final Long ITEM_7 = 70l;
   static final Long ITEM_8 = 80l;
   static final Long ITEM_9 = 90l;
   static final Long ITEM_10 = 100l;
   static final Long ITEM_11 = 110l;

   private static FastByIDMap<PreferenceArray> usersData() {
      FastByIDMap<PreferenceArray> result = new FastByIDMap<PreferenceArray>();

      // User 0
      BooleanUserPreferenceArray preferenceArray = new BooleanUserPreferenceArray(5);
      Long userId = 0l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_1);
      preferenceArray.setItemID(1, ITEM_2);
      preferenceArray.setItemID(2, ITEM_3);
      preferenceArray.setItemID(3, ITEM_4);
      preferenceArray.setItemID(4, ITEM_5);
      result.put(userId, preferenceArray);

      // User 1
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 1l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_9);
      preferenceArray.setItemID(1, ITEM_6);
      preferenceArray.setItemID(2, ITEM_7);
      preferenceArray.setItemID(3, ITEM_8);
      preferenceArray.setItemID(4, ITEM_10);
      result.put(userId, preferenceArray);

      // User 2
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 2l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_1);
      preferenceArray.setItemID(1, ITEM_2);
      preferenceArray.setItemID(2, ITEM_3);
      preferenceArray.setItemID(3, ITEM_4);
      preferenceArray.setItemID(4, ITEM_9);
      result.put(userId, preferenceArray);


      // User 3
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 3l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_1);
      preferenceArray.setItemID(1, ITEM_2);
      preferenceArray.setItemID(2, ITEM_3);
      preferenceArray.setItemID(3, ITEM_5);
      preferenceArray.setItemID(4, ITEM_9);
      result.put(userId, preferenceArray);

      // User 4
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 4l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_5);
      preferenceArray.setItemID(1, ITEM_6);
      preferenceArray.setItemID(2, ITEM_7);
      preferenceArray.setItemID(3, ITEM_10);
      preferenceArray.setItemID(4, ITEM_11);
      result.put(userId, preferenceArray);

      // User 5
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 5l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_1);
      preferenceArray.setItemID(1, ITEM_2);
      preferenceArray.setItemID(2, ITEM_3);
      preferenceArray.setItemID(3, ITEM_6);
      preferenceArray.setItemID(4, ITEM_7);
      result.put(userId, preferenceArray);


      // User 6
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 6l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_1);
      preferenceArray.setItemID(1, ITEM_2);
      preferenceArray.setItemID(2, ITEM_3);
      preferenceArray.setItemID(3, ITEM_10);
      preferenceArray.setItemID(4, ITEM_11);
      result.put(userId, preferenceArray);

      // User 7
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 7l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_5);
      preferenceArray.setItemID(1, ITEM_6);
      preferenceArray.setItemID(2, ITEM_7);
      preferenceArray.setItemID(3, ITEM_10);
      preferenceArray.setItemID(4, ITEM_11);
      result.put(userId, preferenceArray);

      // User 8
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 8l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_1);
      preferenceArray.setItemID(1, ITEM_2);
      preferenceArray.setItemID(2, ITEM_3);
      preferenceArray.setItemID(3, ITEM_5);
      preferenceArray.setItemID(4, ITEM_9);
      result.put(userId, preferenceArray);


      // User 9
      preferenceArray = new BooleanUserPreferenceArray(5);
      userId = 9l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_1);
      preferenceArray.setItemID(1, ITEM_2);
      preferenceArray.setItemID(2, ITEM_3);
      preferenceArray.setItemID(3, ITEM_7);
      preferenceArray.setItemID(4, ITEM_9);
      result.put(userId, preferenceArray);

       // User 10
      preferenceArray = new BooleanUserPreferenceArray(0);
      userId = 10l;
      preferenceArray.setUserID(0, userId);
      result.put(userId, preferenceArray);

      // User 11
      preferenceArray = new BooleanUserPreferenceArray(1);
      userId = 11l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, ITEM_1);
      result.put(userId, preferenceArray);

      // User 12 // no one as this pref
      preferenceArray = new BooleanUserPreferenceArray(1);
      userId = 12l;
      preferenceArray.setUserID(0, userId);
      preferenceArray.setItemID(0, 999l);
      result.put(userId, preferenceArray);

      return result;
   }

}
