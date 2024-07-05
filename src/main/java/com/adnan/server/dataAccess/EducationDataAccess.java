package com.adnan.server.dataAccess;

import com.adnan.server.models.Education;

import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EducationDataAccess {
    private final Connection connection;
    public EducationDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }
    public void addEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO educations (userId, institutionName, major, startDate, endDate, grade, activities, extraExplanation) VALUES (?,?,?,?,?,?,?,?)");
        statement.setString(1, education.getUserId());
        statement.setString(2, education.getInstitutionName());
        statement.setString(3, education.getMajor());
        statement.setDate(4, java.sql.Date.valueOf(education.getStartDate()));
        statement.setDate(5, java.sql.Date.valueOf(education.getEndDate()));
        statement.setDouble(6, education.getGrade());
        statement.setString(7, education.getActivities());
        statement.setString(8, education.getExtraExplanation());
        statement.executeUpdate();
    }
    public void updateEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE educations SET institutionName = ?, major = ?, startDate = ?, endDate = ?, grade = ?, activities = ?, extraExplanation = ?");
        statement.setString(1, education.getInstitutionName());
        statement.setString(2, education.getMajor());
        statement.setDate(3, java.sql.Date.valueOf(education.getStartDate()));
        statement.setDate(4, java.sql.Date.valueOf(education.getEndDate()));
        statement.setDouble(5, education.getGrade());
        statement.setString(6, education.getActivities());
        statement.setString(7, education.getExtraExplanation());
        statement.executeUpdate();
    }
    public void deleteEducation(String userId, Date startDate) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM educations where userId = ? AND startDate = ?");
        statement.setString(1, userId);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        statement.setDate(2, java.sql.Date.valueOf(formatter.format(startDate)));
        statement.executeUpdate();
    }
    public ArrayList<Education> getEducation(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM educations WHERE userId = ?");
        statement.setString(1, userId);
        ArrayList<Education> educations = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Education education = new Education();
            education.setUserId(userId);
            education.setInstitutionName(resultSet.getString("institutionName"));
            education.setMajor(resultSet.getString("major"));
            education.setStartDate(resultSet.getDate("startDate"));
            education.setEndDate(resultSet.getDate("endDate"));
            education.setGrade(resultSet.getDouble("grade"));
            education.setActivities(resultSet.getString("activities"));
            education.setExtraExplanation(resultSet.getString("extraExplanation"));
            educations.add(education);
        }
        return educations;
    }
    public ArrayList<Education> getEducations() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM educations");
        ArrayList<Education> educations = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Education education = new Education();
            education.setUserId(resultSet.getString("userId"));
            education.setInstitutionName(resultSet.getString("institutionName"));
            education.setMajor(resultSet.getString("major"));
            education.setStartDate(resultSet.getDate("startDate"));
            education.setEndDate(resultSet.getDate("endDate"));
            education.setGrade(resultSet.getDouble("grade"));
            education.setActivities(resultSet.getString("activities"));
            education.setExtraExplanation(resultSet.getString("extraExplanation"));
            educations.add(education);
        }
        return educations;
    }
    public boolean educationExists(String userId, Date startDate) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select exists(select *from educations where userId = ? AND startDate = ?)");
        statement.setString(1, userId);
        statement.setDate(2, startDate);
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        while (resultSet.next()) {
            a = resultSet.getInt(1);
        }
        return a == 1;
    }
}


