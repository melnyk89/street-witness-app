package com.kynlem.solution.streetwitness.dao;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kynlem.solution.streetwitness.IncidentAdapter;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
    public void getIncidents(DataSourceCallBackInterface callback) {
        List<Incident> data = new ArrayList<>();
        data.add(new Incident());
        Log.i("HERE", "ldldldlld");
        Executor executor = new Executor(callback);
        executor.execute();
    }

    private class Executor extends AsyncTask<Void, Void, ArrayList<Incident>> {

        private ArrayList<Incident> data;
        private DataSourceCallBackInterface callBackInterface;

        public Executor(DataSourceCallBackInterface callBackInterface){
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
                Log.i("Data size", String.valueOf(data.size()));

                return request.getIncidents();

            } catch (Exception e) {
                Log.e("IncidentsActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Incident> incidents) {
            Log.i("Data size in Post", String.valueOf(data.size()));
            callBackInterface.onIncidentsLoaded(data);
        }
    }
}