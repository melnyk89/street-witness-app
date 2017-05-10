package com.kynlem.solution.streetwitness.addincident;

import com.kynlem.solution.streetwitness.dao.DataSourceInterface;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;
import com.kynlem.solution.streetwitness.dao.Location;
import com.kynlem.solution.streetwitness.incidents.IncidentsContract;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by oleh on 10.05.17.
 */

public class AddIncidentPresenter implements AddIncidentContract.Presenter{

    private final IncidentsRemoteDataSource remoteDataSource;
    private final AddIncidentContract.View addIncidentView;

    public AddIncidentPresenter(@NotNull IncidentsRemoteDataSource incidentsRemoteDataSource,
                               @NotNull AddIncidentContract.View addIncidentView){
        this.remoteDataSource = incidentsRemoteDataSource;
        this.addIncidentView = addIncidentView;
    }

    @Override
    public void saveIncident(String title, String description, Location location) {
        Map<String, Object> dataToSave = new LinkedHashMap<>();
        dataToSave.put("title", title);
        dataToSave.put("description", description);
        dataToSave.put("location", location);
        remoteDataSource.saveIncident(dataToSave);
        addIncidentView.showIncidentsList();
    }

    @Override
    public void start() {

    }
}
