package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.IsTrue;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * @author Jean-Baptiste Lemée
 */
public enum Category {
    OTHER, MOVIE, MUSIC, PEOPLE, ARTICLE, WEBSITE, IMAGE, VIDEO, ACCESSORY, BOOK, LIFESTYLE;
}
