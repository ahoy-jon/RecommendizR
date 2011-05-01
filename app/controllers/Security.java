package controllers;

import models.User;
import play.Logger;
import play.data.validation.Required;
import play.db.jpa.GenericModel.JPAQuery;
import play.libs.Crypto;

public class Security extends Secure.Security {

    public static void ajaxLogin(@Required String login,
                                 @Required String password, boolean remember) throws Throwable {
        // Check tokens
        User user = findUser(login);

        if (authenticate(password, user)) {
            connect(user, remember);
            renderText(user.username);
        } else {
            forbidden();
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
            renderText(user.username);
        } else {
            notFound();
        }
    }

    public static User connectedUser() {
        return findUser(Secure.connected());
    }

    static void connect(User user, boolean rememberme) {
        // Mark user as connected
        session.put(Secure.LOGIN_KEY, user.username);
        if (rememberme) {
            response.setCookie("rememberme", Crypto.sign(user.username) + "-"
                    + user.username, "30d");
        }
    }

    static boolean authenticate(String login, String password) {
        User user = findUser(login);
        return authenticate(password, user);
    }

    static User findUser(String login) {
        JPAQuery query = User.find("byUsername", login);
        logMultipleUsers(login, query);
        User user = query.first();
        if (user == null) {
            user = User.find("byEmail", login).first();
        }
        return user;
    }

    protected static void logMultipleUsers(String login, JPAQuery query) {
        if (query.fetch().size() > 1) {
            Logger.error("user :%s is not unique", login);
        }
    }

    static boolean authenticate(String password, User user) {
        if(user != null && user.password.equals(Crypto.passwordHash(password))){
            return true;
        } else if(user != null && user.password.equals(password)){
             // hack de migration a supprimer dans un certain temps...
            user.password = Crypto.passwordHash(password);
            user.passwordConfirm = Crypto.passwordHash(user.passwordConfirm);
            user.save();
            return true;
        } else {
            return false;
        }
    }

    static boolean check(String profile) {
        if (profile.equals("jblemee"))
            return session.get("username").equals("jblemee");
        return false;
    }
}