package services;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;

public final class CrossingRecommenderBuilder implements RecommenderBuilder {
  
  public Recommender buildRecommender(DataModel dataModel) throws TasteException {
    return new CrossingRecommender(dataModel);
  }
  
}