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

    public Category category;
    @Required @MinSize(3) public String name;
    @Required public String description;
    @MinSize(4) public String usefullFor;
    @MinSize(4) public String usefullWhen;

    public String toString() {
        return name;
    }
}
