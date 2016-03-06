package app.com.ecandle.veeltaxi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import app.com.ecandle.veeltaxi.R;
import app.com.ecandle.veeltaxi.Util.AllConstants;
import app.com.ecandle.veeltaxi.model.DriverModel;
import app.com.ecandle.veeltaxi.model.M;
import app.com.ecandle.veeltaxi.webservices.APIService;
import app.com.ecandle.veeltaxi.webservices.DriverAPI;
import retrofit.Callback;
import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity {
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    AdView adView;
    AdRequest bannerRequest, fullScreenAdRequest;
    InterstitialAd fullScreenAdd;


    EditText edtTaxiId;
    ImageButton imbSearch;
    ConnectionDetector connectionDetector;
    TextView tvwEmail;
    /**
     * Instancia del drawer
     */
    private DrawerLayout drawerLayout;

    /**
     * Titulo inicial del drawer
     */
    private String drawerTitle;


    String emailType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView tvwEmail = (TextView) findViewById(R.id.email);
        if (M.getPrimaryEmail(this) != null){
            tvwEmail.setText(M.getPrimaryEmail(this));
            Log.i(TAG+"M.getPrimaryEmail:",M.getPrimaryEmail(this));
        }
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

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
        //adView.setAdSize(AdSize.FULL_BANNER);
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


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        String title = menuItem.getTitle().toString();
                        //selectItem(title);
                        setSelectedOption(menuItem);
                        return true;
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i(TAG,"LOGOUT TB id:"+id);

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        if (id == AllConstants.draw_logout){
            M.setUsername(null, MainActivity.this);
            M.setPassword(null, MainActivity.this);
            Intent i3 = new Intent(this, LoginActivity.class);
            finish();
            startActivity(i3);
            overridePendingTransition(0, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSelectedOption(MenuItem menuItem){

//        menuItem.setChecked(true);
//        drawerLayout.closeDrawers();


        switch (menuItem.getItemId()) {
            case R.id.email_prim_edit:
                emailType=AllConstants.EmailTypePrimary;
                showChangeEmailDialog(emailType);
                break;
            case R.id.email_alt1_edit:
                emailType=AllConstants.EmailTypeAlternative1;
                showChangeEmailDialog(emailType);
                break;
            case R.id.email_alt2_edit:
                emailType=AllConstants.EmailTypeAlternative2;
                showChangeEmailDialog(emailType);
                break;
            case R.id.email_alt3_edit:
                emailType=AllConstants.EmailTypeAlternative3;
                showChangeEmailDialog(emailType);
                break;
            case R.id.email_alt4_edit:
                emailType=AllConstants.EmailTypeAlternative4;
                showChangeEmailDialog(emailType);
                break;
            case R.id.nav_log_out :
                M.setUsername(null, MainActivity.this);
                M.setPassword(null, MainActivity.this);
                Intent i3 = new Intent(this, LoginActivity.class);
                finish();
                startActivity(i3);
                overridePendingTransition(0,0);
        }



        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    public void showChangeEmailDialog(final String EmailType) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.email_edit_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edt_edit_email);
        // Get email from Shared Preferences
        if (EmailType.equals(AllConstants.EmailTypePrimary)) {
            dialogBuilder.setMessage(getString(R.string.EmailPrimary));
            if (M.getPrimaryEmail(this) != null){
                edt.setText(M.getPrimaryEmail(this));
                Log.i(TAG + "-M.getPrimaryEmail:", M.getPrimaryEmail(this));
            }

        }
        if (EmailType.equals(AllConstants.EmailTypeAlternative1)) {
            dialogBuilder.setMessage(getString(R.string.EmailAlternative1));
            if (M.getAlternativeEmail1(this) != null){
                edt.setText(M.getAlternativeEmail1(this));
                Log.i(TAG + "M.getAlternativeEmail1:", M.getAlternativeEmail1(this));
            }
        }
        if (EmailType.equals(AllConstants.EmailTypeAlternative2)) {
            dialogBuilder.setMessage(getString(R.string.EmailAlternative2));
            if (M.getAlternativeEmail2(this) != null){
                edt.setText(M.getAlternativeEmail2(this));
                Log.i(TAG + "M.getAlternativeEmail2:", M.getAlternativeEmail2(this));
            }
        }
        if (EmailType.equals(AllConstants.EmailTypeAlternative3)) {
            dialogBuilder.setMessage(getString(R.string.EmailAlternative3));
            if (M.getAlternativeEmail3(this) != null){
                edt.setText(M.getAlternativeEmail3(this));
                Log.i(TAG + "M.getAlternativeEmail3:", M.getAlternativeEmail3(this));
            }
        }
        if (EmailType.equals(AllConstants.EmailTypeAlternative4)) {
            dialogBuilder.setMessage(getString(R.string.EmailAlternative4));
            if (M.getAlternativeEmail4(this) != null){
                edt.setText(M.getAlternativeEmail4(this));
                Log.i(TAG + "M.getAlternativeEmail4:", M.getAlternativeEmail4(this));
            }
        }
        dialogBuilder.setTitle(getResources().getString(R.string.EmailChangeDialog));

        dialogBuilder.setPositiveButton(getString(R.string.DoneText), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                String email = edt.getText().toString().trim();

                if (isValidEmail(email)) {
                    // Guardar email valido en Shared Preferences
                    if (EmailType.equals(AllConstants.EmailTypePrimary)) {
                        // Guardar email valido en Shared Preferences
                        M.setPrimaryEmail(email, MainActivity.this);

                    }
                    if (EmailType.equals(AllConstants.EmailTypeAlternative1)) {
                        M.setAlternativeEmail1(email, MainActivity.this);
                    }
                    if (EmailType.equals(AllConstants.EmailTypeAlternative2)) {
                        M.setAlternativeEmail2(email, MainActivity.this);
                    }
                    if (EmailType.equals(AllConstants.EmailTypeAlternative3)) {
                        M.setAlternativeEmail3(email, MainActivity.this);
                    }
                    if (EmailType.equals(AllConstants.EmailTypeAlternative4)) {
                        M.setAlternativeEmail4(email, MainActivity.this);
                    }
                    M.showToast(MainActivity.this, getString(R.string.EmailChanged));
                } else {
                    edt.setError(getResources().getString(R.string.InvalidEmail));
                    M.showToast(MainActivity.this, getString(R.string.InvalidEmail));
                }

            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.CancelText), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
