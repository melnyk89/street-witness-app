package com.kynlem.solution.streetwitness.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by oleh on 26.04.17.
 */

public class RequestContainer {
    @JsonProperty("count")
    private String count;

    @JsonProperty("incidents")
    private Incident[] incidents;

    public String getCount(){
        return  count;
    }

    public ArrayList<Incident> getIncidents() {
        ArrayList<Incident> incidentsList = null;
        if(incidents.length > 0){
            incidentsList = new ArrayList<Incident>();
            for(int i = 0; i < incidents.length; i++){
                incidentsList.add(incidents[i]);
            }
        }
        return incidentsList;
    }
}
