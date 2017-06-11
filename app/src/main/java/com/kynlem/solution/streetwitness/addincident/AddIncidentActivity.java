package com.kynlem.solution.streetwitness.addincident;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kynlem.solution.streetwitness.R;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;
import com.kynlem.solution.streetwitness.dao.Location;

public class AddIncidentActivity extends AppCompatActivity implements AddIncidentContract.View,
        LocationListener {

    private AddIncidentContract.Presenter addIncidentPresenter;
    private EditText editIncidentTitle;
    private EditText editIncidentDescription;
    private EditText editIncidentLat;
    private EditText editIncidentLong;
    private ImageButton btnSaveNewIncident;
    private LocationManager locationManager;
    private android.location.Location currentLocation;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_incident);
        editIncidentTitle = (EditText) findViewById(R.id.editTitle);
        editIncidentDescription = (EditText) findViewById(R.id.editDescription);
        editIncidentLat = (EditText) findViewById(R.id.editLatitude);
        editIncidentLong = (EditText) findViewById(R.id.editLongitude);
        btnSaveNewIncident = (ImageButton) findViewById(R.id.btnSaveIncident);
        addIncidentPresenter = new AddIncidentPresenter(IncidentsRemoteDataSource.getInstance(), this);

        btnSaveNewIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = String.valueOf(editIncidentTitle.getText());
                String description = String.valueOf(editIncidentDescription.getText());
                String lat = String.valueOf(editIncidentLat.getText());
                String lng = String.valueOf(editIncidentLong.getText());
                addIncidentPresenter.saveIncident(title, description, new Location(lat, lng));
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                               ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                                return;
        }
        currentLocation = locationManager.getLastKnownLocation(provider);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("________HERE_________", "Here in start");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
    }

    @Override
    public void showIncidentsList() {
        this.setResult(Activity.RESULT_OK);
        this.finish();
    }

    @Override
    public void setPresenter(AddIncidentContract.Presenter presenter) {
        this.addIncidentPresenter = presenter;
    }

    @Override
    public boolean checkInternetConnection() {
        return false;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Toast.makeText(this, "Location changed" + location.getLatitude(),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Status changed",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                              Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                       // TODO: Consider calling
                        return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }
}
