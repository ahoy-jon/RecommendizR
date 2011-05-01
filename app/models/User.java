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

    @Required @MinSize(6) public String username;
    @Required public String firstname;
    @Required public String lastname;
    @Required @MinSize(6) public String password;
    @Required @Equals("password") public String passwordConfirm;
    @Required @Email public String email;
    @Required @Equals("email") public String emailConfirm;
    @Required @IsTrue public boolean termsOfUse;
    public String company;

    public String toString() {
        return username;
    }
}
