package com.kynlem.solution.streetwitness.incidents_map;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kynlem.solution.streetwitness.dao.DataSourceInterface;
import com.kynlem.solution.streetwitness.dao.Incident;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;

import java.util.ArrayList;


/**
 * Created by oleh on 05.05.17.
 */

public class IncidentsMapPresenter implements IncidentsMapContract.Presenter {

    private final IncidentsRemoteDataSource remoteDataSource;
    private final IncidentsMapContract.View incidentsMapView;

    public IncidentsMapPresenter(@NonNull IncidentsRemoteDataSource incidentsRemoteDataSource,
                                 @NonNull IncidentsMapContract.View incidentsView) {
        this.remoteDataSource = incidentsRemoteDataSource;
        this.incidentsMapView = incidentsView;
        incidentsView.setPresenter(this);
    }

    @Override
    public void loadIncidents() {
        if (incidentsMapView.isTimeToUpdate()) {
            remoteDataSource.getIncidents(new DataSourceInterface.DataSourceCallBackInterface() {
                @Override
                public void onIncidentsLoaded(ArrayList<Incident> incidents) {
                    incidentsMapView.showIncidents(incidents);
                }

                @Override
                public String onTokenRequired() {
                    return incidentsMapView.getStoredTokenFromPreferences();
                }
            });
        }
    }

    @Override
    public void start() {
        loadIncidents();
    }
}
