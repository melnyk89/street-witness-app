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

    public String getLat() {
        return lat;
    }

    public String getLng(){
        return lng;
    }
}
