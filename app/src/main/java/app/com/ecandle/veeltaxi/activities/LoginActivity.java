package app.com.ecandle.veeltaxi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import app.com.ecandle.veeltaxi.R;
import app.com.ecandle.veeltaxi.model.LoginModel;
import app.com.ecandle.veeltaxi.model.M;
import app.com.ecandle.veeltaxi.webservices.APIService;
import app.com.ecandle.veeltaxi.webservices.AuthenticationAPI;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by Jtomaylla on 2015-02-03.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
    EditText etusername, etpassword, etemail;
    Button btnlogin, btnSaltearse;
    ImageButton  imbAdmin;
    LinearLayout tblRegister,tblLogin;
    String username, password, email;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connectionDetector = new ConnectionDetector(this);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnSaltearse = (Button) findViewById(R.id.btnSaltearse);
        imbAdmin = (ImageButton) findViewById(R.id.imbAdmin);

        etusername = (EditText) findViewById(R.id.etusername);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        etusername.setHint(getString(R.string.UserNameHint));

        tblRegister = (LinearLayout) findViewById(R.id.tblRegister);
        tblLogin = (LinearLayout) findViewById(R.id.tblLogin);

        btnlogin.setOnClickListener(this);
        btnSaltearse.setOnClickListener(this);
        imbAdmin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnlogin) {
            username = etusername.getText().toString().trim();
            password = etpassword.getText().toString().trim();
            if (username.length() <= 2) {
                etusername.setError(getString(R.string.InvalidUsername));
            } else if (password.length() <= 5) {
                etpassword.setError(getString(R.string.InvalidPassword));
            } else {
                login();
            }
        }
        if (v.getId() == R.id.btnSaltearse) {
            email = etemail.getText().toString().trim();
            if (isValidEmail(email)) {
                // Guardar email valido en Shared Preferences
                Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);

                startActivity(mIntent);
                finish();
            }else{
                etemail.setError(getResources().getString(R.string.InvalidEmail));
            }

        }

        if (v.getId() == R.id.imbAdmin) {
            if (tblRegister.getVisibility() == View.VISIBLE) {
                tblRegister.setVisibility(View.INVISIBLE);
                tblLogin.setVisibility(View.VISIBLE);
            } else {
                tblRegister.setVisibility(View.VISIBLE);
                tblLogin.setVisibility(View.INVISIBLE);
            }

        }
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void login()
    {
        if(connectionDetector.isConnectingToInternet()) {
            M.showLoadingDialog(this);
            AuthenticationAPI mAuthenticationAPI = APIService.createService(AuthenticationAPI.class);
            mAuthenticationAPI.login(username, password, new Callback<LoginModel>() {
                @Override
                public void success(LoginModel loginModel, retrofit.client.Response response) {
                    M.hideLoadingDialog();

                    System.out.println(" test response---" + response.getUrl());


                    if (loginModel.getSuccess().equals("1")) {

                        M.setID(loginModel.getUserid(), LoginActivity.this);
                        Log.e("uid", loginModel.getUserid() + "");
                        M.setPassword(password, LoginActivity.this);
                        M.setUsername(username, LoginActivity.this);
                        Intent mIntent = new Intent(LoginActivity.this, MainActivity.class ); //GetAllTables.class

                        startActivity(mIntent);
                        finish();
                    } else {
                        M.showToast(LoginActivity.this, "Username or Password Invalid");
                    }

                }

                @Override
                public void failure(RetrofitError error) {


                    M.hideLoadingDialog();
                    M.T(LoginActivity.this, getString(R.string.ServerError));
                    M.T(LoginActivity.this, ""+error.getMessage());
                }
            });
        }else
        {
            M.showToast(LoginActivity.this, "No Internet Connection");
        }
    }



}