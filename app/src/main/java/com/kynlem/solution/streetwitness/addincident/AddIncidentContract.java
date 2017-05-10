package com.kynlem.solution.streetwitness.addincident;

import com.kynlem.solution.streetwitness.BasePresenter;
import com.kynlem.solution.streetwitness.BaseView;
import com.kynlem.solution.streetwitness.dao.DataSourceInterface;
import com.kynlem.solution.streetwitness.dao.Location;

/**
 * Created by oleh on 10.05.17.
 */

public interface AddIncidentContract {

    interface View extends BaseView<Presenter>{
        void showIncidentsList();
    }

    interface Presenter extends BasePresenter{
        void saveIncident(String title, String description, Location location);
    }
}
