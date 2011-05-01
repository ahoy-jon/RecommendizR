package controllers;

import static Utils.Redis.newConnection;

import models.User;
import play.Logger;
import play.data.validation.Required;
import play.db.jpa.GenericModel.JPAQuery;
import play.libs.Crypto;
import play.libs.OpenID;
import redis.clients.jedis.Jedis;

public class Security extends Secure.Security {

    /**
    * Réalise l'authentification.
    * Le parametre action ne sert à rien ?
    */
   public static void authenticateOpenId(String action, String openid_identifier) {
      if (OpenID.isAuthenticationResponse()) {
         OpenID.UserInfo verifiedUser = OpenID.getVerifiedID();
         if (verifiedUser == null) {
            flash.error("Erreur OpenID generique");
            forbidden("Erreur OpenID generique");
         }

         String userEmail = verifiedUser.extensions.get("email");
         if (userEmail == null) {
            final String errorMessage = "L'identification de votre compte sur le site des Zindeps s'effectue avec votre email." +
                    " Vous devez authoriser le domaine recommendizr.com à accéder à votre email pour vous authentifier.";
            flash.error(errorMessage);
            Logger.info(errorMessage);
            forbidden(errorMessage);
         }

         User user = User.findByMail(userEmail);
         if (user == null) {
            Logger.info("User creation for email : " + userEmail);
            user = new User(userEmail);
            user.save();
            Jedis jedis = newConnection();
            jedis.sadd("users", String.valueOf(user.id));
         }

         connect(user, true);

         renderText(user.email);

      } else {
         if (openid_identifier == null) {
            flash.error("Param openid_identifier is null");
            forbidden("Param openid_identifier is null");
         }
         if (openid_identifier.trim().isEmpty()) {
            flash.error("Param openid_identifier is empty");
            forbidden("Param openid_identifier is empty");
         }

         // Verify the id
         if (!OpenID.id(openid_identifier).required("email", "http://axschema.org/contact/email").verify()) {
            flash.put("error", "Impossible de s'authentifier avec l'URL utilisée.");
            forbidden("Impossible de s'authentifier avec l'URL utilisée.");
         }
      }
   }

   public static void logout() throws Throwable {
      session.clear();
      response.removeCookie("rememberme");
      flash.success("secure.logout");
      Application.index();
   }

   public static void userName() throws Throwable {
      User user = connectedUser();
      if (null != user) {
         renderText(user.email);
      } else {
         notFound();
      }
   }

   public static User connectedUser() {
      return findUser(Secure.connected());
   }

   static void connect(User user, boolean rememberme) {
      // Mark user as connected
      session.put(Secure.LOGIN_KEY, user.email);
      if (rememberme) {
         response.setCookie("rememberme", Crypto.sign(user.email) + "-"
                 + user.email, "30d");
      }
   }

   static User findUser(String mail) {
      User user = User.findByMail(mail);
      return user;
   }

   protected static void logMultipleUsers(String login, JPAQuery query) {
      if (query.fetch().size() > 1) {
         Logger.error("user :%s is not unique", login);
      }
   }

   static boolean check(String profile) {
      if (profile.equals("jblemee"))
         return session.get("username").equals("jblemee");
      return false;
   }
}