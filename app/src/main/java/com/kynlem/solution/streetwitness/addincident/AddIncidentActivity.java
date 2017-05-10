package com.kynlem.solution.streetwitness.addincident;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kynlem.solution.streetwitness.R;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;
import com.kynlem.solution.streetwitness.dao.Location;

public class AddIncidentActivity extends AppCompatActivity implements AddIncidentContract.View{

    private AddIncidentContract.Presenter addIncidentPresenter;
    private EditText editIncidentTitle;
    private EditText editIncidentDescription;
    private EditText editIncidentLat;
    private EditText editIncidentLong;
    private ImageButton btnSaveNewIncident;


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
}
