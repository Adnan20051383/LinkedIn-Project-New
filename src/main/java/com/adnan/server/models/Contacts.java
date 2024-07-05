package com.adnan.server.models;

import com.adnan.server.enums.BirthdayDisplayPolicy;
import com.adnan.server.enums.PhoneNumberType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.dnd.DropTarget;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Contacts {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("address")
    private String address;
    @JsonProperty("phoneNumberType")
    private PhoneNumberType phoneNumberType;
    @JsonProperty("birthday")
    private Date birthday;
    @JsonProperty("birthdayDisplayPolicy")
    private BirthdayDisplayPolicy birthdayDisplayPolicy;

    public Contacts(String userId, String email, String phoneNumber, String address, String birthday) throws ParseException {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
    }
    public Contacts() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PhoneNumberType getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(PhoneNumberType phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }
    public String getBirthday() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(birthday);
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public BirthdayDisplayPolicy getBirthdayDisplayPolicy() {
        return birthdayDisplayPolicy;
    }

    public void setBirthdayDisplayPolicy(BirthdayDisplayPolicy birthdayDisplayPolicy) {
        this.birthdayDisplayPolicy = birthdayDisplayPolicy;
    }
}
