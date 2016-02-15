package app.com.ecandle.veeltaxi.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

import app.com.ecandle.veeltaxi.R;
import app.com.ecandle.veeltaxi.model.DriverModel;
import app.com.ecandle.veeltaxi.model.M;
import app.com.ecandle.veeltaxi.webservices.APIService;
import app.com.ecandle.veeltaxi.webservices.DriverAPI;
import retrofit.Callback;
import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity {
    private Context con;
    private GoogleMap googleMap;


    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private TextView lblLocation;
    double latitude;
    double longitude;

    AdView adView;
    AdRequest bannerRequest, fullScreenAdRequest;
    InterstitialAd fullScreenAdd;

//    private LinearLayout atms, banks, bookstores, busstations, cafes, carwash,
//            dentist, doctor, food, gasstation, grocery, gym, hospitals,
//            mosques, theater, park, pharmacy, police, restaurant, school, mall,
//            spa, store, university;

    private ShareActionProvider myShareActionProvider;
    // Activate when use GeoLocation

    //LocationFound updateLoc;

    EditText edtTaxiId;
    ImageButton imbSearch;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionDetector = new ConnectionDetector(this);
        edtTaxiId = (EditText) findViewById(R.id.edtTaxiId);
        imbSearch = (ImageButton) findViewById(R.id.imbSearch);


        imbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String driver_id = edtTaxiId.getText().toString();
                searchTaxiId(driver_id);
            }
        });

        enableAd();

    }

    private void searchTaxiId(String driver_id) {


        if(connectionDetector.isConnectingToInternet()) {
            M.showLoadingDialog(this);
            DriverAPI mDriverAPI = APIService.createService(DriverAPI.class);
            mDriverAPI.search(driver_id, new Callback<DriverModel>() {
                @Override
                public void success(DriverModel driverModel, retrofit.client.Response response) {
                    M.hideLoadingDialog();

                    System.out.println(" test response---" + response.getUrl());


                    if (driverModel.getSuccess().equals("1")) {

                        //M.setID(driverModel.getDriver_id(), MainActivity.this);
                        Log.i("driver_id", driverModel.getDriver_id() + "");
                        Log.i("first_name", driverModel.getFirst_name() + "");
                        Log.i("company_name", driverModel.getCompany_name() + "");
                        Log.i("photo_driver_front", driverModel.getPhoto_driver_front() + "");
                        Log.i("driver_row",driverModel.toString());
                        //M.setPassword(password, MainActivity. this);
                        //M.setUsername(username, MainActivity.this);
                        Bundle extras = new Bundle();
                        extras.putString("mDriverId",driverModel.getDriver_id());
                        extras.putString("mName", driverModel.getFirst_name());
                        extras.putString("mPhotoLink",driverModel.getPhoto_driver_front());
                        extras.putString("mCompany", driverModel.getCompany_name());

                        Intent mIntent = new Intent(MainActivity.this, Main2Activity.class); //GetAllTables.class
                        mIntent.putExtras(extras);
                        startActivity(mIntent);
                        finish();
                    } else {
                        M.showToast(MainActivity.this, getString(R.string.noTaxiId));
                    }

                }

                @Override
                public void failure(RetrofitError error) {


                    M.hideLoadingDialog();
                    M.T(MainActivity.this, getString(R.string.ServerError));
                    M.T(MainActivity.this, "" + error.getMessage());
                    Log.i("error", error.getMessage());
                }
            });
        }else
        {
            M.showToast(MainActivity.this, getString(R.string.NoInternetConnection));
        }


    }
    private void enableAd() {
        // TODO Auto-generated method stub

        // adding banner add
        adView = (AdView) findViewById(R.id.adView);
        bannerRequest = new AdRequest.Builder().build();
        adView.loadAd(bannerRequest);

        // adding full screen add
//        fullScreenAdd = new InterstitialAd(this);
//        fullScreenAdd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
//        fullScreenAdRequest = new AdRequest.Builder().build();
//        fullScreenAdd.loadAd(fullScreenAdRequest);
//
//        fullScreenAdd.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdLoaded() {
//
//                Log.i("FullScreenAdd", "Loaded successfully");
//                fullScreenAdd.show();
//
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//
//                Log.i("FullScreenAdd", "failed to Load");
//            }
//        });

        // TODO Auto-generated method stub

    }

}
