package com.kynlem.solution.streetwitness.incidents_map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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


import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 42);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 42 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            Log.i("IMAGE", "Uri: " + uri);

            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:b75dac16-691b-4cf4-a62c-7775d6183fdf",
                    Regions.US_EAST_1
            );

            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
            TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());

            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {
                    split[1]
            };


            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else {


            }
            String dataColum = getDataColumn(getApplicationContext(), contentUri, selection, selectionArgs);
            Log.i("IMAGE", "path: " + dataColum);


            File fileToUpload = new File(dataColum);

            TransferObserver observer = transferUtility.upload(
                    "streetwitness",   /* The bucket to upload to */
                    "test_android_" + new Date().getTime() + ".jpg",    /* The key for the uploaded object */
                    fileToUpload        /* The file where the data to upload exists */
            );


            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.i("State", String.valueOf(state));
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    Log.i("Progress", String.valueOf(bytesCurrent));
                }

                @Override
                public void onError(int id, Exception ex) {
                    Log.i("Error ----",  ex.getMessage());

                }
            });

        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
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
    public void showLoginWindow() {
        final Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
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
        Log.i("TOKEN ---- ", token);
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
