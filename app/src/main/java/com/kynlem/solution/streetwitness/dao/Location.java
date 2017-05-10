package com.kynlem.solution.streetwitness.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by oleh on 26.04.17.
 */

public class Location {

    @JsonProperty("lat")
    private String lat;

    @JsonProperty("lng")
    private String lng;

    public Location(){}

    public Location(final String lat, final String lng){
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng(){
        return lng;
    }

    @Override
    public String toString() {
        return "{lat: " + lat + ", lng: " + lng + "}";
    }
}
