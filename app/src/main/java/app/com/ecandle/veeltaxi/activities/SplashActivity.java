package app.com.ecandle.veeltaxi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import app.com.ecandle.veeltaxi.R;
import app.com.ecandle.veeltaxi.model.LoginModel;
import app.com.ecandle.veeltaxi.model.M;
import app.com.ecandle.veeltaxi.webservices.APIService;
import app.com.ecandle.veeltaxi.webservices.AuthenticationAPI;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashActivity extends AppCompatActivity {
    /** Called when the activity is first created. */
    private final Handler mHandler = new Handler();
    private static final int duration = 6000;

    private static final String TAG = SplashActivity.class.getSimpleName();

    String username, password;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler.postDelayed(mPendingLauncherRunnable,
                SplashActivity.duration);

        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        }
        connectionDetector = new ConnectionDetector(this);
        if (!connectionDetector.isConnectingToInternet()) {

            M.showToast(this, "No Internet Connection");
            finish();
        } else {


//            if (M.getUsername(this) == null) {
//                Intent mIntent = new Intent(this, LoginActivity.class);
//                startActivity(mIntent);
//                finish();
//            } else {
//
//                username = M.getUsername(this);
//                password = M.getPassword(this);
//                //checkuser();
//                checkPrimaryEMail();
//            }
            checkPrimaryEMail();
        }
    }

    public void checkuser() {
        AuthenticationAPI mAuthenticationAPI = APIService.createService(AuthenticationAPI.class);
        mAuthenticationAPI.login(username, password, new Callback<LoginModel>() {
            @Override
            public void success(LoginModel loginModel, Response response) {
                System.out.println("response===" + loginModel.getUserid());
                if (loginModel.getSuccess().equals("1")) {

                    M.setID(loginModel.getUserid(), SplashActivity.this);

//                    Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
                    Intent mIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mIntent);
                    finish();
                } else {
                    Intent mIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mIntent);
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Intent mIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mIntent);
                finish();

            }
        });
    }

    public void checkPrimaryEMail(){
        if (M.getPrimaryEmail(this) != null){
            Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mIntent);
            finish();
            Log.i(TAG + "-M.getPrimaryEmail:", M.getPrimaryEmail(this));
        } else {
            Intent mIntent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(mIntent);
            finish();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mPendingLauncherRunnable);

    }

    private final Runnable mPendingLauncherRunnable = new Runnable() {

        public void run() {
//            final Intent intent = new Intent(SplashActivity.this,
//                    LoginActivity.class);
            final Intent intent = new Intent(SplashActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}