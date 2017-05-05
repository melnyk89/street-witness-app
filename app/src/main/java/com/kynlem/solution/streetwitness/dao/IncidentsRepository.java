package com.kynlem.solution.streetwitness.dao;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by oleh on 05.05.17.
 */

public class IncidentsRepository implements IncidentsRepositoryContract {

    @Override
    public void getTasks(@NonNull AsyncTask<Void, Void, ArrayList<Incident>> callback) {
        callback = new AsyncTask<Void, Void, ArrayList<Incident>>() {
            @Override
            protected ArrayList<Incident> doInBackground(Void... params) {
                try {
                    final String url = "http://street-witness.herokuapp.com/api/incidents/";
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<RequestContainer> requestResponse =
                            restTemplate.exchange(url,
                                    HttpMethod.GET, null, new ParameterizedTypeReference<RequestContainer>() {
                                    });
                    RequestContainer request = requestResponse.getBody();

                    return request.getIncidents();
                } catch (Exception e) {
                    Log.e("IncidentsActivity", e.getMessage(), e);
                }
                return null;
            }
        };
    }
}
