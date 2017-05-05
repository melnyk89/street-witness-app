package com.kynlem.solution.streetwitness.incidents;

import com.kynlem.solution.streetwitness.BasePresenter;
import com.kynlem.solution.streetwitness.BaseView;
import com.kynlem.solution.streetwitness.dao.Incident;

import java.util.List;

/**
 * Created by oleh on 04.05.17.
 */

public interface IncidentsContract {

    interface View extends BaseView<Presenter> {
        void showIncidents(List<Incident> incidents);
    }

    interface Presenter extends BasePresenter {
        void loadIncidents(boolean forceUpdate);

        void addNewIncident();
    }
}
