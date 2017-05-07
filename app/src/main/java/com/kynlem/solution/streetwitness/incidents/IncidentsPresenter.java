package com.kynlem.solution.streetwitness.incidents;

import android.support.annotation.NonNull;

import com.kynlem.solution.streetwitness.dao.Incident;
import com.kynlem.solution.streetwitness.dao.DataSourceInterface;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;

import java.util.ArrayList;


/**
 * Created by oleh on 05.05.17.
 */

public class IncidentsPresenter implements IncidentsContract.Presenter {

    private final IncidentsRemoteDataSource remoteDataSource;
    private final IncidentsContract.View incidentsView;

    public IncidentsPresenter(@NonNull IncidentsRemoteDataSource incidentsRemoteDataSource,
                              @NonNull IncidentsContract.View tasksView) {
        this.remoteDataSource = incidentsRemoteDataSource;
        incidentsView = tasksView;
        incidentsView.setPresenter(this);
    }

    @Override
    public void loadIncidents() {
        if(incidentsView.checkInternetConnection()) {
            remoteDataSource.getIncidents(new DataSourceInterface.DataSourceCallBackInterface() {
                @Override
                public void onIncidentsLoaded(ArrayList<Incident> incidents) {
                    incidentsView.showIncidents(incidents);
                }
            });
        }
    }

    @Override
    public void addNewIncident() {

    }

    @Override
    public void start() {

    }
}
