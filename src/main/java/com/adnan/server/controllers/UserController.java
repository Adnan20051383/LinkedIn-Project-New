package com.adnan.server.controllers;

import com.adnan.server.dataAccess.BioDataAccess;
import com.adnan.server.dataAccess.FollowDataAccess;
import com.adnan.server.dataAccess.UserDataAccess;
import com.adnan.server.models.Bio;
import com.adnan.server.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController {
    private static UserDataAccess UDA = null;
    private static BioDataAccess BDA = null;
    private static FollowDataAccess FDA = null;
    public UserController() throws SQLException {
        UDA  = new UserDataAccess();
        BDA = new BioDataAccess();
        FDA = new FollowDataAccess();
    }
    public String getUsers() throws SQLException, JsonProcessingException {
        ArrayList<User> users = UDA.getUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        return  objectMapper.writeValueAsString(users);
    }
    public String getUser(String id) throws SQLException, JsonProcessingException {
        User user = UDA.getUser(id);
        if (user == null) return "No User";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }
    public String getUser(String id, String password) throws SQLException, JsonProcessingException {
        User user = UDA.getUser(id, password);
        if (user == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }
    private static boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (email == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public String createUser(String id, String firstName, String lastName, String additionalName, String country, String city, String email, String password, String phoneNumber) throws SQLException {
        if (!isValidEmail(email))
            return "NOT A VALID EMAIL!";
        User user = new User(id, firstName, lastName, additionalName, "" ,"" ,   country, city, email, password, phoneNumber, FDA.countFollowers(id), FDA.countFollowings(id));
        if (UDA.userExists(id))
            UDA.updateUser(user);
        else
            UDA.addUser(user);
        return "successful!";
    }
    public String deleteUser(String id) throws SQLException {
        if(UDA.userExists(id)) {
            UDA.deleteUser(UDA.getUser(id));
            return "successful!";
        }
        return "NO USER!";
    }
    public String createBio(String userId, String bioText, String location) throws SQLException {
        if(!UDA.userExists(userId))
            return "USER NOT FOUND!";

        Bio bio = new Bio(userId, bioText, location);

        if(BDA.bioExists(userId))
            BDA.updateBio(bio);
        else
            BDA.addBio(bio);
        return "successful!";
    }
    public String getBio(String id) throws SQLException, JsonProcessingException {
        Bio bio = BDA.getBio(id);
        if (bio == null) return "NO BIO!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(bio);
    }
    public String getBios() throws SQLException, JsonProcessingException {
        ArrayList<Bio> bios = BDA.getBios();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(bios);
    }
    public String deleteBio(String userId) throws SQLException {
        if(BDA.bioExists(userId)) {
            BDA.deleteBio(userId);
            return "successful!";
        }
        else
            return "NO BIO FOUND!";
    }
    public boolean userExists(String id) throws SQLException {
        return UDA.userExists(id);
    }


}
