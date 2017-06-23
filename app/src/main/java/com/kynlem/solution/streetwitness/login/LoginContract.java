package com.kynlem.solution.streetwitness.login;

import com.kynlem.solution.streetwitness.BasePresenter;
import com.kynlem.solution.streetwitness.BaseView;


/**
 * Created by oleh on 22.06.17.
 */

public interface LoginContract {

    interface View extends BaseView<LoginContract.Presenter> {
        void loginUser(String token);
        void wrongCredentials();
    }

    interface Presenter extends BasePresenter {
        void loginUser(String user, String password);
    }
}
