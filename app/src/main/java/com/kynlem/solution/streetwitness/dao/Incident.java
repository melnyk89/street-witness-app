package com.kynlem.solution.streetwitness.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by oleh on 25.04.17.
 */


public class Incident {

    @JsonProperty("description")
    private String description;

    @JsonProperty("id")
    private String id;

    @JsonProperty("location")
    private Location location;

    @JsonProperty("status")
    private String status;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("title")
    private String title;


    public String getTitle(){
        return  this.title;
    }

    public String getDescription() { return this.description; }

    public String getStatus() { return this.status; }

    public String getTimestamp() {return this.timestamp; }

    public  String getLocation() {
        return this.location.getLat() + " " + this.location.getLng();
    }

}
