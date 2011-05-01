package models;

import javax.persistence.Entity;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * @author Jean-Baptiste Lemée
 */
@Entity
public class Liked extends Model {

    @Required public Category category;
    @Required @MinSize(3) public String name;
    @Required public String description;
    @Required @MinSize(4) public String usefullFor;
    @Required @MinSize(4) public String usefullWhen;

    public String toString() {
        return name;
    }
}
