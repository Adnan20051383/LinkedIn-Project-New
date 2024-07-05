package com.adnan.server.dataAccess;

import com.adnan.server.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDataAccess {
    private final Connection connection;
    private final FollowDataAccess FDA;

    public UserDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
        FDA = new FollowDataAccess();
    }
    public void addUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id, firstName, lastName, additionalName, country, city, email, password, phoneNumber, followers, followings) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, user.getId());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getAdditionalName());
        statement.setString(5, user.getCountry());
        statement.setString(6, user.getCity());
        statement.setString(7, user.getEmail());
        statement.setString(8, user.getPassword());
        statement.setString(9, user.getPhoneNumber());
        statement.setInt(10, FDA.countFollowers(user.getId()));
        statement.setInt(11, FDA.countFollowings(user.getId()));
        statement.executeUpdate();
        statement.close();
    }
    public void deleteUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        statement.setString(1, user.getId());
        statement.executeUpdate();
    }

    public boolean userExists(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select exists(select *from users where id = ?)");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        while (resultSet.next()) {
            a = resultSet.getInt(1);
        }
        return a == 1;
    }

    public User getUser(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setFirstName(resultSet.getString("firstName"));
            user.setLastName(resultSet.getString("lastName"));
            user.setAdditionalName(resultSet.getString("additionalName"));
            user.setCountry(resultSet.getString("country"));
            user.setCity(resultSet.getString("city"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setPhoneNumber(resultSet.getString("phoneNumber"));
            user.setFollowers(FDA.countFollowers(resultSet.getString("id")));
            user.setFollowings(FDA.countFollowings(resultSet.getString("id")));
            return user;
        }

        return null;
    }
    public User getUser(String id, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select *from users where id = ? and password = ?");
        statement.setString(1,id);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setFirstName(resultSet.getString("firstName"));
            user.setLastName(resultSet.getString("lastName"));
            user.setAdditionalName(resultSet.getString("additionalName"));
            user.setCountry(resultSet.getString("country"));
            user.setCity(resultSet.getString("city"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setPhoneNumber(resultSet.getString("phoneNumber"));
            user.setFollowers(FDA.countFollowers(resultSet.getString("id")));
            user.setFollowings(FDA.countFollowings(resultSet.getString("id")));
            return user;
        }
        return null;
    }
    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setFirstName(resultSet.getString("firstName"));
            user.setLastName(resultSet.getString("lastName"));
            user.setAdditionalName(resultSet.getString("additionalName"));
            user.setCountry(resultSet.getString("country"));
            user.setCity(resultSet.getString("city"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setPhoneNumber(resultSet.getString("phoneNumber"));
            user.setFollowers(FDA.countFollowers(resultSet.getString("id")));
            user.setFollowings(FDA.countFollowings(resultSet.getString("id")));
            users.add(user);
        }
        return users;
    }
    public void deleteUser(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from users where id = ?");
        statement.setString(1, id);
        statement.executeUpdate();
    }

    public void updateUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET firstName = ?, lastName = ?, additionalName = ?, country = ?, city = ?, email = ?, password = ?, phoneNumber = ?, followers = ?, followings = ? WHERE id = ?");
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getAdditionalName());
        statement.setString(4, user.getCountry());
        statement.setString(5, user.getCity());
        statement.setString(6, user.getEmail());
        statement.setString(7, user.getPassword());
        statement.setString(8, user.getPhoneNumber());
        statement.setInt(9, FDA.countFollowers(user.getId()));
        statement.setInt(10, FDA.countFollowings(user.getId()));
        statement.setString(11, user.getId());

        statement.executeUpdate();
    }
    public void updateFollower(String followed) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET followers = ? WHERE id = ?");
        int a = FDA.countFollowers(followed);
        statement.setInt(1, a);
        statement.setString(2, followed);
        statement.executeUpdate();
    }
    public void updateFollowing(String follower) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET followings = ? WHERE id = ?");
        int a = FDA.countFollowings(follower);
        statement.setInt(1, a);
        statement.setString(2, follower);
        statement.executeUpdate();
    }
}

