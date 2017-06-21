package com.kynlem.solution.streetwitness.dao;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oleh on 05.05.17.
 */

public interface DataSourceInterface {

    interface DataSourceCallBackInterface {
        void onIncidentsLoaded(ArrayList<Incident> incidents);
        String onTokenRequired();
    }

    void getIncidents(DataSourceCallBackInterface loadCallback);

    void saveIncident(Map<String, Object> dataToStore);
}
