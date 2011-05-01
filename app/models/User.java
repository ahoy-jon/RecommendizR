package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.apache.commons.lang.StringUtils;

import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.IsTrue;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * @author Jean-Baptiste Lemée
 */
@Entity
public class User extends Model {

   @Required
   @Email
   public String email;

   public User(Long id) {
      super();
      this.id = id;
   }

    public User(String email) {
      super();
      this.email = email;
   }

   public String toString() {
      return email;
   }

   /**
    * Recherche par email.
    */
   public static User findByMail(String mail) {
      if (mail == null) {
         return null;
      }
      return User.find("from User z where email=:mail").bind("mail", mail.trim().toLowerCase()).first();
   }
}
