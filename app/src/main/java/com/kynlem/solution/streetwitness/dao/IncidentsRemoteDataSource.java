package com.kynlem.solution.streetwitness.dao;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oleh on 05.05.17.
 */

public class IncidentsRemoteDataSource implements DataSourceInterface {

    private static IncidentsRemoteDataSource INSTANCE = null;
    private static String token = "";

    private IncidentsRemoteDataSource() {}

    public static IncidentsRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new IncidentsRemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getIncidents(DataSourceCallBackInterface callback) {
        String query = "http://street-witness.herokuapp.com/api/incidents/";
        GetRequestExecutor getIncidentsQuery = new GetRequestExecutor(callback, query);
        getIncidentsQuery.execute();
    }

    @Override
    public void saveIncident(Map<String, Object> dataToStore) {
        PostExecutor executor = new PostExecutor("http://street-witness.herokuapp.com/api/incidents/");
        executor.execute(dataToStore);
    }

    private class PostExecutor extends AsyncTask<Map<String, Object>, Void, String> {
        private String url;

        public PostExecutor(final String url){
            this.url = url;
        }

        @Override
        protected String doInBackground(Map<String, Object>... params) {
            RestTemplate restTemplate = new RestTemplate();
            HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
            HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

            restTemplate.getMessageConverters().add(formHttpMessageConverter);
            restTemplate.getMessageConverters().add(stringHttpMessageConverter);

            return restTemplate.postForObject(this.url, params[0], String.class);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ONPOstExecute - ", result);
        }
    }

    private class GetRequestExecutor extends AsyncTask<Void, Void, ArrayList<Incident>> {
        private ArrayList<Incident> data;
        private String url;
        private DataSourceCallBackInterface callBackInterface;

        public GetRequestExecutor(final DataSourceCallBackInterface callBackInterface,
                                  final String url){
            this.callBackInterface = callBackInterface;
            this.url = url;
        }

        @Override
        protected ArrayList<Incident> doInBackground(Void... params) {
            try {
                token = callBackInterface.onTokenRequired();
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("content-type", "application/json");
                headers.add("Authorization", "Bearer " + token);
                //headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE0OTgwMjUzNjgsImV4cCI6MTQ5ODExMTc2OCwic3ViIjoyLCJyb2xlIjoiVXNlciJ9.tuQyolk3uEcVB43FLj-nVfoUR_fY0a1JAJirgfyzw0Y");
                HttpEntity entity = new HttpEntity(headers);
                ResponseEntity<RequestContainer> requestResponse =
                        restTemplate.exchange(url,
                                HttpMethod.GET, entity, new ParameterizedTypeReference<RequestContainer>() {
                                });
                RequestContainer request = requestResponse.getBody();
                data = request.getIncidents();
                return request.getIncidents();

            } catch (HttpStatusCodeException e) {
                switch (e.getStatusCode()){
                    case UNAUTHORIZED:
                        Log.i("AUTHORIZATION FAILED", e.getStatusCode().toString());
                        break;
                    default:
                        Log.i("FAULT", e.getResponseBodyAsString());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Incident> incidents) {
            callBackInterface.onIncidentsLoaded(data);
        }
    }
}