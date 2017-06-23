package com.kynlem.solution.streetwitness.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kynlem.solution.streetwitness.R;
import com.kynlem.solution.streetwitness.dao.IncidentsRemoteDataSource;


public class LoginActivity extends AppCompatActivity implements LoginContract.View{
    private Button btnLoginUser;
    private Button btnRegisterUser;
    private EditText editUserName;
    private EditText editUserPassword;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLoginUser = (Button) findViewById(R.id.btnLogin);
        btnRegisterUser = (Button) findViewById(R.id.btnRegister);
        editUserName = (EditText) findViewById(R.id.editUserName);
        editUserPassword = (EditText) findViewById(R.id.editUserPassword);
        loginPresenter = new LoginPresenter(IncidentsRemoteDataSource.getInstance(), this);

        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i, 1);

        btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.loginUser(editUserName.getText().toString(),
                        editUserPassword.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.i("Picture=====", picturePath);
            cursor.close();
        }
    }

    @Override
    public void loginUser(String token) {
        SharedPreferences settings = getSharedPreferences("network_params", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("TOKEN", token);
        editor.commit();
    }

    @Override
    public void wrongCredentials() {
        Toast.makeText(this, "Wrong Username or password. Please try again", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }

    @Override
    public boolean checkInternetConnection() {
        return false;
    }
}
