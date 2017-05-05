package com.kynlem.solution.streetwitness.incidents;

import android.support.annotation.NonNull;

import com.kynlem.solution.streetwitness.dao.Incident;
import com.kynlem.solution.streetwitness.dao.IncidentsRepository;

/**
 * Created by oleh on 05.05.17.
 */

public class IncidentsPresenter implements IncidentsContract.Presenter {

    private final IncidentsRepository mIncidentsRepository;
    private final IncidentsContract.View mIncidentsView;

    public IncidentsPresenter(@NonNull IncidentsRepository tasksRepository,
                              @NonNull IncidentsContract.View tasksView) {
        mIncidentsRepository = tasksRepository;
        mIncidentsView = tasksView;
        mIncidentsView.setPresenter(this);
    }

    @Override
    public void loadIncidents(boolean forceUpdate) {
        
    }

    @Override
    public void addNewIncident() {

    }

    @Override
    public void start() {
        loadIncidents(false);
    }
}
