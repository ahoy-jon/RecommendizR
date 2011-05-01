package services;

import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CachingUserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * A simple {@link Recommender}
 */
public final class CrossingBooleanRecommender implements Recommender {

  private final Recommender recommender;

  public CrossingBooleanRecommender(DataModel dModel) throws TasteException {
    UserSimilarity similarity = new CachingUserSimilarity(new LogLikelihoodSimilarity(dModel), dModel);
    UserNeighborhood neighborhood =
        new NearestNUserNeighborhood(10, Double.NEGATIVE_INFINITY, similarity, dModel, 1.0);
    recommender = new GenericBooleanPrefUserBasedRecommender(dModel, neighborhood, similarity);
  }

  public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
    return recommender.recommend(userID, howMany);
  }

  public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer) throws TasteException {
    return recommender.recommend(userID, howMany, rescorer);
  }

  public float estimatePreference(long userID, long itemID) throws TasteException {
    return recommender.estimatePreference(userID, itemID);
  }

  public void setPreference(long userID, long itemID, float value) throws TasteException {
    recommender.setPreference(userID, itemID, value);
  }

  public void removePreference(long userID, long itemID) throws TasteException {
    recommender.removePreference(userID, itemID);
  }

  public DataModel getDataModel() {
    return recommender.getDataModel();
  }

  public void refresh(Collection<Refreshable> alreadyRefreshed) {
    recommender.refresh(alreadyRefreshed);
  }

  @Override
  public String toString() {
    return "CrossingBooleanRecommender[recommender:" + recommender + ']';
  }

}