package com.adnan.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Education {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("institutionName")
    private  String institutionName;
    @JsonProperty("major")
    private String major;
    @JsonProperty("startDate")
    private Date startDate;
    @JsonProperty("endDate")
    private Date endDate;
    @JsonProperty("grade")
    private double grade;
    @JsonProperty("activities")
    private String activities;
    @JsonProperty("extraExplanation")
    private String extraExplanation;

    public Education(String userId, String institutionName, String major, String startDate, String endDate, double grade, String activities, String extraExplanation) throws ParseException {
        this.userId = userId;
        this.institutionName = institutionName;
        this.major = major;
        this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        this.grade = grade;
        this.activities = activities;
        this.extraExplanation = extraExplanation;
    }
    public Education() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getStartDate() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getExtraExplanation() {
        return extraExplanation;
    }

    public void setExtraExplanation(String extraExplanation) {
        this.extraExplanation = extraExplanation;
    }
}
