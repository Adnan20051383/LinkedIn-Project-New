package com.adnan.server.enums;

public enum BirthdayDisplayPolicy {
    JUST_FOR_ME("just me"),
    JUST_CONTACTS("just contacts"),
    PUBLIC("public");
    private final String fieldName;

    BirthdayDisplayPolicy(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
