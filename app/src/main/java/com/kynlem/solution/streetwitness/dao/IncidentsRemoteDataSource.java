package com.kynlem.solution.streetwitness.dao;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oleh on 05.05.17.
 */

public class IncidentsRemoteDataSource implements DataSourceInterface {

    private static IncidentsRemoteDataSource INSTANCE = null;

    private IncidentsRemoteDataSource() {}

    public static IncidentsRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new IncidentsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getIncidents(DataSourceLoadCallBackInterface callback) {
        GetExecutor executor = new GetExecutor(callback);
        executor.execute();
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

    private class GetExecutor extends AsyncTask<Void, Void, ArrayList<Incident>> {

        private ArrayList<Incident> data;
        private DataSourceLoadCallBackInterface callBackInterface;

        public GetExecutor(DataSourceLoadCallBackInterface callBackInterface){
            this.callBackInterface = callBackInterface;
        }

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
                data = request.getIncidents();
                return request.getIncidents();

            } catch (Exception e) {
                Log.e("IncidentsActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Incident> incidents) {
            callBackInterface.onIncidentsLoaded(data);
        }
    }
}