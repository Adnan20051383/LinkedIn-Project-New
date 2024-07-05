package com.adnan.server.enums;

public enum PhoneNumberType {
    CELLPHONE("cellphone"),
    HOME("home"),
    WORK("work");
    private final String fieldName;
    PhoneNumberType(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getFieldName() {
        return fieldName;
    }
}
