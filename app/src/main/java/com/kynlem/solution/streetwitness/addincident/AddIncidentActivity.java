package com.kynlem.solution.streetwitness.addincident;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.kynlem.solution.streetwitness.R;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;
import com.kynlem.solution.streetwitness.dao.Location;

import java.text.SimpleDateFormat;

public class AddIncidentActivity extends AppCompatActivity implements AddIncidentContract.View, LocationListener {

    private AddIncidentContract.Presenter addIncidentPresenter;
    private Spinner spinnerIncidentType;
    private EditText editIncidentDescription;
    private ImageButton btnSaveNewIncident;
    private ImageButton btnTakePicture;
    private LocationManager locationManager;
    private android.location.Location currentLocation;
    private double lat;
    private double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_incident);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
        spinnerIncidentType = (Spinner) findViewById(R.id.spinnerTitle);
        editIncidentDescription = (EditText) findViewById(R.id.editDescription);
        btnSaveNewIncident = (ImageButton) findViewById(R.id.btnSaveIncident);
        btnTakePicture = (ImageButton) findViewById(R.id.btnTakePhoto);
        addIncidentPresenter = new AddIncidentPresenter(IncidentsRemoteDataSource.getInstance(), this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final double longitudeFromMarker = getIntent().getDoubleExtra("markerLng", 0);
        final double latitudeFromMarker = getIntent().getDoubleExtra("markerLat", 0);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.incidents_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIncidentType.setAdapter(adapter);


        btnSaveNewIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = spinnerIncidentType.getSelectedItem().toString();
                String description = String.valueOf(editIncidentDescription.getText());

                if (longitudeFromMarker == 0 &&
                        latitudeFromMarker == 0) {
                    if (currentLocation != null) {
                        lat = currentLocation.getLatitude();
                        lng = currentLocation.getLongitude();
                        addIncidentPresenter.saveIncident(title, description,
                                new Location(String.valueOf(lat), String.valueOf(lng)));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "GPS data is not ready. Try again", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    addIncidentPresenter.saveIncident(title, description,
                            new Location(String.valueOf(latitudeFromMarker),
                                    String.valueOf(longitudeFromMarker)));
                }
            }
        });


        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        if (longitudeFromMarker == 0 && longitudeFromMarker == 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        if (location != null) {
            Toast.makeText(this, "location - " +
                            location.getLatitude() +
                            ", " +
                            location.getLongitude(),
                    Toast.LENGTH_LONG).show();

            currentLocation = location;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager.removeUpdates(this);
            locationManager = null;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Status", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "provider", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
