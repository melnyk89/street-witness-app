package com.kynlem.solution.streetwitness.incidents_map;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kynlem.solution.streetwitness.R;
import com.kynlem.solution.streetwitness.addincident.AddIncidentActivity;
import com.kynlem.solution.streetwitness.dao.Incident;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;
import com.kynlem.solution.streetwitness.incidents.IncidentsActivity;
import com.kynlem.solution.streetwitness.incidents.IncidentsContract;
import com.kynlem.solution.streetwitness.incidents.IncidentsPresenter;

import java.util.ArrayList;

public class IncidentsMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        IncidentsContract.View{

    private GoogleMap mMap;
    private Toolbar toolbar;
    private IncidentsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        presenter = new IncidentsPresenter(IncidentsRemoteDataSource.getInstance(), this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final Intent addIncidentIntent = new Intent(getApplicationContext(), AddIncidentActivity.class);
                startActivity(addIncidentIntent);
                return false;
            }
        });

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_history:
                final Intent historyIntent = new Intent(getApplicationContext(), IncidentsActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.action_refresh:
                break;
            case R.id.action_about:
                Toast.makeText(this, "Street witness 1.0", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showIncidents(ArrayList<Incident> incidents) {
        Toast.makeText(this, "Data was fetched on map. Size - " + incidents.size(), Toast.LENGTH_LONG).show();
        for (Incident incident: incidents){
            com.kynlem.solution.streetwitness.dao.Location location = incident.getLocationObj();
            LatLng incidentPosition = new LatLng(new Double(location.getLat()), new Double(location.getLng()));
            System.out.println(incidentPosition);
            mMap.addMarker(new MarkerOptions().position(incidentPosition).title(incident.getTitle() + ": " +
                    incident.getDescription()));
        }
    }

    @Override
    public void setPresenter(IncidentsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean checkInternetConnection() {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadIncidents();
    }
}
