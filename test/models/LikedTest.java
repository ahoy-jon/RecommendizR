package models;


import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashSet;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LikedTest {
   private static final Logger log = LoggerFactory.getLogger(LikedTest.class);
   private HashSet<Liked> likedList;
   private Jedis jedis;
   private User user;

   @Before
   public void before() {
      jedis = mock(Jedis.class);
      user = new User("test@test.com");
      likedList = new HashSet<Liked>();
      Liked iLike = new Liked();
      iLike.id = 1l;
      Liked iDontLike = new Liked();
      iDontLike.id = 2l;
      likedList.add(iLike);
      likedList.add(iDontLike);
      when(jedis.hget("u1", "like:l1")).thenReturn("toto");
      when(jedis.hget("u1", "like:l2")).thenReturn(null);

   }

   @Test
   public void fillWhenUserIsConnected() {
      user.id = 1l;
      Liked.fill(likedList, user, jedis);
      for (Liked liked : likedList) {
         if (liked.getId() == 1l) {
            assertTrue(liked.liked);
         } else {
            assertEquals(false, (boolean) liked.liked);
         }
      }
   }

   @Test
   public void fillWhenUserIsNotConnected() {
      user = null;
      Liked.fill(likedList, user, jedis);
      for (Liked liked : likedList) {
         assertNull(liked.liked);
      }
   }
}
