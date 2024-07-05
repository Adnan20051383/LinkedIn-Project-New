package com.adnan.server.dataAccess;

import com.adnan.server.models.Contacts;
import com.adnan.server.models.Education;

import java.sql.*;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ContactsDataAccess {
    private final Connection connection;
    public ContactsDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }
    public void addContacts(Contacts contacts) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO contacts (userId, email, phoneNumber, address, birthday) VALUES (?,?,?,?,?)");
        statement.setString(1, contacts.getUserId());
        statement.setString(2, contacts.getEmail());
        statement.setString(3, contacts.getPhoneNumber());
        statement.setString(4, contacts.getAddress());
        statement.setDate(5, java.sql.Date.valueOf(contacts.getBirthday()));
        statement.executeUpdate();
    }
    public void updateContacts(Contacts contacts) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE contacts SET email = ?, phoneNumber = ?, address = ?, birthday = ?");
        statement.setString(1, contacts.getEmail());
        statement.setString(2, contacts.getPhoneNumber());
        statement.setString(3, contacts.getAddress());
        statement.setDate(4, java.sql.Date.valueOf(contacts.getBirthday()));
        statement.executeUpdate();
    }
    public void deleteContacts(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM contacts where userId = ?");
        statement.setString(1, userId);
        statement.executeUpdate();
    }
    public Contacts getContacts(String userId) throws SQLException, ParseException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE userId = ?");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();
        Contacts contacts = null;
        if (resultSet.next()) {
            contacts = new Contacts();
            contacts.setUserId(userId);
            contacts.setEmail(resultSet.getString("email"));
            contacts.setPhoneNumber(resultSet.getString("phoneNumber"));
            contacts.setAddress("address");
            contacts.setBirthday(resultSet.getDate("birthday"));
        }
        return contacts;
    }
    public ArrayList<Contacts> getContacts() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts");
        ArrayList<Contacts> contacts = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Contacts contact = new Contacts();
            contact = new Contacts();
            contact.setUserId(resultSet.getString("userId"));
            contact.setEmail(resultSet.getString("email"));
            contact.setPhoneNumber(resultSet.getString("phoneNumber"));
            contact.setAddress("address");
            contact.setBirthday(resultSet.getDate("birthday"));
            contacts.add(contact);
        }
        return contacts;
    }
    public boolean contactsExists(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select exists(select *from contacts where userId = ?)");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        while (resultSet.next()) {
            a = resultSet.getInt(1);
        }
        return a == 1;
    }
}
