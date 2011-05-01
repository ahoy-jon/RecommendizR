package Utils;

import play.Play;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

/**
 * @author Jean-Baptiste lemée
 */
public class Redis {

   static final JedisShardInfo redisConfig = new JedisShardInfo(
           Play.configuration.getProperty("redis.host", "localhost"),
           Integer.valueOf(Play.configuration.getProperty("redis.port", "6379")));

   static {
      redisConfig.setPassword(Play.configuration.getProperty("redis.password", "foobared"));
   }

   public static Jedis newConnection(){
      return new Jedis(redisConfig);
   }
}
