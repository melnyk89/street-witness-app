package com.kynlem.solution.streetwitness.incidents;

import com.kynlem.solution.streetwitness.BasePresenter;
import com.kynlem.solution.streetwitness.BaseView;
import com.kynlem.solution.streetwitness.dao.Incident;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oleh on 04.05.17.
 */

public interface IncidentsContract {

    interface View extends BaseView<Presenter> {
        void showIncidents(ArrayList<Incident> incidents);
        boolean isTimeToUpdate();
        String getStoredTokenFromPreferences();
        void showLoginScreen();
    }

    interface Presenter extends BasePresenter {
        void loadIncidents();
    }
}
