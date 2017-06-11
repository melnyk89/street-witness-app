package com.kynlem.solution.streetwitness.incidents;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.kynlem.solution.streetwitness.IncidentAdapter;
import com.kynlem.solution.streetwitness.R;
import com.kynlem.solution.streetwitness.addincident.AddIncidentActivity;
import com.kynlem.solution.streetwitness.dao.DataSourceInterface;
import com.kynlem.solution.streetwitness.dao.Incident;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;
import com.kynlem.solution.streetwitness.dao.Location;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class IncidentsActivity extends AppCompatActivity implements IncidentsContract.View {

    private Toolbar toolbar;
    private ListView incidentsList;
    private IncidentAdapter incidentAdapter;
    private IncidentsContract.Presenter presenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        incidentsList = (ListView) findViewById(R.id.incidents_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        setSupportActionBar(toolbar);
        presenter = new IncidentsPresenter(IncidentsRemoteDataSource.getInstance(), this);
        final Intent intent = new Intent(this, AddIncidentActivity.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadIncidents();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadIncidents();
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

    @Override
    public void showIncidents(ArrayList<Incident> incidents) {
        incidentAdapter = new IncidentAdapter(this, incidents);
        incidentsList.setAdapter(incidentAdapter);
        Toast.makeText(this, "Data was fetched. Size - " + incidents.size(), Toast.LENGTH_LONG).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(IncidentsContract.Presenter presenter) {
        this.presenter =  presenter;
    }

    @Override
    public boolean checkInternetConnection() {
        if (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        else
            return false;
    }
}
