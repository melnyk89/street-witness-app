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
                              @NonNull IncidentsContract.View incidentsView) {
        this.remoteDataSource = incidentsRemoteDataSource;
        this.incidentsView = incidentsView;
        incidentsView.setPresenter(this);
    }

    @Override
    public void loadIncidents() {
        remoteDataSource.getIncidents(new DataSourceInterface.DataSourceCallBackInterface() {
            @Override
            public void onIncidentsLoaded(ArrayList<Incident> incidents) {
                incidentsView.showIncidents(incidents);
            }

            @Override
            public void onLoginRequired() {
                incidentsView.showLoginScreen();
            }

            @Override
            public String onTokenRequired() {
                return incidentsView.getStoredTokenFromPreferences();
            }

        });
    }

    @Override
    public void start() {
        loadIncidents();
    }
}
