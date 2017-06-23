package com.kynlem.solution.streetwitness.login;

import android.support.annotation.NonNull;

import com.kynlem.solution.streetwitness.dao.DataSourceInterface;
import com.kynlem.solution.streetwitness.dao.Incident;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;

import java.util.ArrayList;

/**
 * Created by oleh on 22.06.17.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final IncidentsRemoteDataSource remoteDataSource;
    private final LoginContract.View loginView;

    public LoginPresenter(@NonNull IncidentsRemoteDataSource incidentsRemoteDataSource,
                          @NonNull LoginContract.View loginView) {
        this.remoteDataSource = incidentsRemoteDataSource;
        this.loginView = loginView;
        loginView.setPresenter(this);
    }

    @Override
    public void loginUser(String user, String password) {
        remoteDataSource.loginUser(user, password, new DataSourceInterface.LoginCallBackInterface() {

            @Override
            public void onLogin(String token) {
                loginView.loginUser(token);
            }

            @Override
            public void onWrongUserNameOrPassword() {
                loginView.wrongCredentials();
            }
        });
    }

    @Override
    public void start() {

    }
}
