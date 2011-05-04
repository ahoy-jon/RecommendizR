package models;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import redis.clients.jedis.Jedis;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Set;

/**
 * @author Jean-Baptiste Lemée
 */
@Entity
public class Liked extends Model {

   public Category category;
   @Required
   @MinSize(3)
   public String name;
   @Required
   public String description;
   @MinSize(4)
   public String usefullFor;
   @MinSize(4)
   public String usefullWhen;
   @Transient
   public Boolean liked;

   public String toString() {
      return name;
   }

   public static boolean isLiked(Long likedId, User user, Jedis jedis) {

      boolean r = null != jedis.hget("u" + user.id, "like:l" + likedId);
      return r;
   }

   public static void fill(Set<Liked> likedList, User user, Jedis jedis) {
      if (user != null) {
         for (Liked item : likedList) {
            item.liked = isLiked(item.getId(), user, jedis);
         }
      }

   }
}
