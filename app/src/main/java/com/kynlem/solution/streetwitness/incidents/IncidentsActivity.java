package com.kynlem.solution.streetwitness.incidents;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.kynlem.solution.streetwitness.IncidentAdapter;
import com.kynlem.solution.streetwitness.R;
import com.kynlem.solution.streetwitness.dao.Incident;
import com.kynlem.solution.streetwitness.dao.RequestContainer;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


public class IncidentsActivity extends AppCompatActivity {

    ListView incidentsList;
    IncidentAdapter incidentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        incidentsList = (ListView) findViewById(R.id.incidents_list);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, ArrayList<Incident>> {
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

        @Override
        protected void onPostExecute(ArrayList<Incident> incidents) {
            Log.i("Executed", String.valueOf(incidents.size()));
            incidentAdapter = new IncidentAdapter(getApplicationContext(), incidents);
            incidentsList.setAdapter(incidentAdapter);
        }
    }
}
