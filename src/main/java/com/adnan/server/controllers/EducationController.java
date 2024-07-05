package com.adnan.server.controllers;

import com.adnan.server.dataAccess.EducationDataAccess;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.adnan.server.dataAccess.UserDataAccess;
import com.adnan.server.models.Education;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EducationController {
    private static EducationDataAccess EDA = null;
    private static UserDataAccess UDA = null;
    private LoggedInUserDataAccess LIUDA = null;

    public EducationController() throws SQLException {
        EDA = new EducationDataAccess();
        UDA = new UserDataAccess();
        LIUDA = new LoggedInUserDataAccess();
    }
    public String createEducation(String userId, String institutionName, String major, String startDate, String endDate, double grade, String activities, String extraExplanation) throws ParseException, SQLException {
        if (!UDA.userExists(userId) || !LIUDA.getUser().equals(userId))
            return "USER NOT FOUND OR NOT ALLOWED!!!!";
        Education education = new Education(userId, institutionName, major, startDate, endDate, grade, activities, extraExplanation);
        if (EDA.educationExists(userId, java.sql.Date.valueOf(startDate)))
            EDA.updateEducation(education);
        else
            EDA.addEducation(education);
        return "successful!";
    }
    public String getEducation(String userId) throws SQLException, JsonProcessingException {
        ArrayList<Education> educations = EDA.getEducation(userId);
        if (educations == null) return "NO EDUCATION FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(educations);
    }
    public String getEducations() throws SQLException, JsonProcessingException {
        ArrayList<Education> educations = EDA.getEducations();
        if (educations == null) return "NO EDUCATION FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(educations);
    }
    public String deleteEducation(String userId, Date startDate) throws SQLException {
        if (!UDA.userExists(userId) || !EDA.educationExists(userId, startDate))
            return "SOMETHING'S WRONG!";
        EDA.deleteEducation(userId, startDate);
        return "successful!";
    }

}
