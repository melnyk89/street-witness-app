package com.kynlem.solution.streetwitness.dao;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by oleh on 05.05.17.
 */

public interface DataSourceInterface {

    interface DataSourceCallBackInterface{
        void onIncidentsLoaded(ArrayList<Incident> incidents);
    }

    void getIncidents(DataSourceCallBackInterface callback);
}
