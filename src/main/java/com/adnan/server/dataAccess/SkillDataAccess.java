package com.adnan.server.dataAccess;

import com.adnan.server.models.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SkillDataAccess {
    private final Connection connection;

    public SkillDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }

    public void addSkill(Skill skill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO skills (userId, nthSkill, text) VALUES (?,?,?)");
        statement.setString(1, skill.getUserId());
        statement.setInt(2, skill.getNthSkill());
        statement.setString(3, skill.getText());
        statement.executeUpdate();
    }
    public int countSkills(String userId) throws SQLException {
        if (!skillExists(userId))
           return 0;
        else {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM skills where userId = ?");
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            int a = 0;
            if (resultSet.next())
                a = resultSet.getInt(1);
            return a;
        }
    }
    public boolean skillExists(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select exists(select *from skills where userId = ?)");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        while (resultSet.next()) {
            a = resultSet.getInt(1);
        }
        return a == 1;
    }
    public ArrayList<Skill> getSkills(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM skills WHERE userId = ? order by nthSkill");
        statement.setString(1, userId);
        ArrayList<Skill> skills = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Skill skill = new Skill();
            skill.setUserId(userId);
            skill.setNthSkill(resultSet.getInt("nthSkill"));
            skill.setText(resultSet.getString("text"));
            skills.add(skill);
        }
        return skills;
    }
    public ArrayList<Skill> getSkills() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM skills order by nthSkill");
        ArrayList<Skill> skills = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Skill skill = new Skill();
            skill.setUserId(resultSet.getString("userId"));
            skill.setNthSkill(resultSet.getInt("nthSkill"));
            skill.setText(resultSet.getString("text"));
            skills.add(skill);
        }
        return skills;
    }
    public void deleteSkill(String userId, int nthSkill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM skills WHERE userId = ? AND nthSkill = ?");
        statement.setString(1, userId);
        statement.setInt(2, nthSkill);
        statement.executeUpdate();
    }
    public void updateSkill(Skill skill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE skills SET text = ? WHERE userId = ? AND nthSkill = ?");
        statement.setString(1, skill.getText());
        statement.setString(2, skill.getUserId());
        statement.setInt(3, skill.getNthSkill());
        statement.executeUpdate();
    }

}
