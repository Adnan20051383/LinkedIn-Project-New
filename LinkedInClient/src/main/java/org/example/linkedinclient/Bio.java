package org.example.linkedinclient;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Bio {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("bioText")
    private String bioText;

    @JsonProperty("location")
    private String location;


    public Bio(String userId, String bioText, String location) {
        this.userId = userId;
        this.bioText = bioText;
        this.location = location;
    }

    public Bio() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBioText() {
        return bioText;
    }

    public void setBioText(String bioText) {
        this.bioText = bioText;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
