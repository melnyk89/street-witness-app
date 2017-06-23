package com.kynlem.solution.streetwitness.incidents_map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.kynlem.solution.streetwitness.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IncidentsMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        IncidentsMapContract.View{

    private GoogleMap mMap;
    private Toolbar toolbar;
    private IncidentsMapContract.Presenter presenter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab_map_view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), AddIncidentActivity.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);
        presenter = new IncidentsMapPresenter(IncidentsRemoteDataSource.getInstance(), this);

//        final Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(loginIntent);

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
                addIncidentIntent.putExtra("markerLng", marker.getPosition().longitude);
                addIncidentIntent.putExtra("markerLat", marker.getPosition().latitude);
                Log.i("+++++++++++ THERE ", String.valueOf(marker.getPosition().longitude));

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
        if (incidents != null) {
            Toast.makeText(this, "Data was fetched on map. Size - " + incidents.size(), Toast.LENGTH_LONG).show();
            for (Incident incident : incidents) {
                com.kynlem.solution.streetwitness.dao.Location location = incident.getLocationObj();
                LatLng incidentPosition = new LatLng(new Double(location.getLat()), new Double(location.getLng()));
//                mMap.addMarker(new MarkerOptions().position(incidentPosition).title(incident.getTitle() + ": " +
//                        incident.getDescription()));
            }
        }
    }

    @Override
    public boolean isTimeToUpdate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date stringDate = simpledateformat.parse();

        return true;
    }

    @Override
    public String getStoredTokenFromPreferences() {
        SharedPreferences settings = getSharedPreferences("network_params", 0);
        String token = settings.getString("TOKEN", "");
        return token;
    }

    @Override
    public void setPresenter(IncidentsMapContract.Presenter presenter) {
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
