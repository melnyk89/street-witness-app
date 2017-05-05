package com.kynlem.solution.streetwitness.dao;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 05.05.17.
 */

public interface IncidentsRepositoryContract {

    interface LoadIncidentsCallback1 {

    }

    void getTasks(@NonNull AsyncTask<Void, Void, ArrayList<Incident>> callback);
}
