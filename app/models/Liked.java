package models;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import redis.clients.jedis.Jedis;

import javax.persistence.Entity;
import javax.persistence.Transient;

import java.util.Collection;
import java.util.Set;

/**
 * @author Jean-Baptiste Lemée
 */
@Entity
public class Liked extends Model {

   @Required
   @MinSize(3)
   public String name;
   @Required
   public String description;
   @Transient
   public Boolean liked;
   @Transient
   public Boolean ignored;

   public String toString() {
      return name;
   }

   public static boolean isLiked(Long likedId, User user, Jedis jedis) {

      boolean r = null != jedis.hget("u" + user.id, "like:l" + likedId);
      return r;
   }

   public static Collection<Liked> fill(Collection<Liked> likedList, User user, Jedis jedis) {
      if (user != null) {
         for (Liked item : likedList) {
            item.liked = isLiked(item.getId(), user, jedis);
            item.ignored = isIgnored(item.getId(), user, jedis);
         }
      }
      return likedList;
   }

   public static boolean isIgnored(Long likedId, User user, Jedis jedis) {
      boolean r = null != jedis.hget("ignore:u" + user.id, "like:l" + likedId);
      return r;
   }
}
