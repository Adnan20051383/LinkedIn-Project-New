package com.adnan.server.controllers;

import com.adnan.server.dataAccess.ContactsDataAccess;
import com.adnan.server.dataAccess.EducationDataAccess;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.adnan.server.dataAccess.UserDataAccess;
import com.adnan.server.models.Contacts;
import com.adnan.server.models.Education;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactsController {
    private static ContactsDataAccess CDA = null;
    private static UserDataAccess UDA = null;
    private LoggedInUserDataAccess LIUDA = null;

    public ContactsController() throws SQLException {
        CDA = new ContactsDataAccess();
        UDA = new UserDataAccess();
        LIUDA = new LoggedInUserDataAccess();
    }
    public String createContacts(String userId, String email, String phoneNumber, String address, String birthday) throws ParseException, SQLException {
        if (!isValidEmail(email))
            return "NOT A VALID EMAIL!!!";
        if (!UDA.userExists(userId) || !LIUDA.getUser().equals(userId))
            return "USER NOT FOUND OR NOT ALLOWED!!!!";
        Contacts contacts = new Contacts(userId, email, phoneNumber, address, birthday);
        if (CDA.contactsExists(userId))
            CDA.updateContacts(contacts);
        else
            CDA.addContacts(contacts);
        return "successful!";
    }
    public String getContacts(String userId) throws SQLException, JsonProcessingException, ParseException {
        Contacts contacts = CDA.getContacts(userId);
        if (contacts == null) return "NO CONTACTS FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(contacts);
    }
    public String getContacts() throws SQLException, JsonProcessingException {
        ArrayList<Contacts> contacts = CDA.getContacts();
        if (contacts == null) return "NO CONTACTS FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(contacts);
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
    public String deleteContacts(String userId) throws SQLException {
        if (!UDA.userExists(userId) || !CDA.contactsExists(userId))
            return "SOMETHING'S WRONG!";
        CDA.deleteContacts(userId);
        return "successful!";
    }

}
