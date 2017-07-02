package com.kynlem.solution.streetwitness.incidents_map;

import com.kynlem.solution.streetwitness.BasePresenter;
import com.kynlem.solution.streetwitness.BaseView;
import com.kynlem.solution.streetwitness.dao.Incident;

import java.util.ArrayList;

/**
 * Created by oleh on 04.05.17.
 */

public interface IncidentsMapContract {

    interface View extends BaseView<Presenter> {
        void showIncidents(ArrayList<Incident> incidents);
        void showLoginWindow();
        boolean isTimeToUpdate();
        String getStoredTokenFromPreferences();
    }

    interface Presenter extends BasePresenter {
        void loadIncidents();
    }
}
