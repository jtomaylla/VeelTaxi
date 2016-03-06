package app.com.ecandle.veeltaxi.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import app.com.ecandle.veeltaxi.R;
import app.com.ecandle.veeltaxi.Util.AllConstants;
import app.com.ecandle.veeltaxi.model.M;

//import com.codeandcoder.finalguide.R;

public class MapsActivity extends FragmentActivity implements LocationListener {

    GoogleMap googleMap;
    private ImageButton imbStartRide;
    private ImageButton imbEndRide;
    String email1;
    String email2;
    String email3;
    String email4;
    String email5;
    double longitude;
    double latitude;
    LocationManager locationManager;
    Criteria criteria;
    String provider;
    Location location;

    String mDriverId,mName,mCompany;
    String latStart;
    String lngStart;

    private CheckBox mTrafficCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (getIntent().getExtras() != null){
            Bundle data = getIntent().getExtras();
            mDriverId = data.getString("mDriverId");
            mName = data.getString("mName");
            mCompany = data.getString("mCompany");
        }

        email1 = M.getPrimaryEmail(this);
        email2 = M.getAlternativeEmail1(this);
        email3 = M.getAlternativeEmail2(this);
        email4 = M.getAlternativeEmail3(this);
        email5 = M.getAlternativeEmail4(this);

        mTrafficCheckbox = (CheckBox) findViewById(R.id.traffic);
        imbStartRide = (ImageButton) findViewById(R.id.imbStartRide);
        imbEndRide = (ImageButton) findViewById(R.id.imbEndRide);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            criteria = new Criteria();

            // Getting the name of the best provider
            provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(this,"GPS Localization error" , Toast.LENGTH_SHORT).show();
                return;
            } else {
                location = locationManager.getLastKnownLocation(provider);
                if (location != null){
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    latStart = String.valueOf(latitude);
                    lngStart = String.valueOf(longitude);
                    String msj = "lon:"+lngStart+"-lat:"+latStart;
                    //Toast.makeText(this,msj , Toast.LENGTH_SHORT).show();
                    Log.i("MapsActivity-msj:", msj);
                    //Send Mail with LatLng
                    if(location!=null){
                        onLocationChanged(location);
                    }
                    locationManager.requestLocationUpdates(provider, 20000, 0, this);

                } else{
                    Toast.makeText(this,"GPS Localization error" , Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        }
    }
    @Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("Golden Gate Bridge")
//                .snippet("San Francisco")
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.mappin)));
        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        // Setting latitude and longitude in the TextView tv_location


    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.testmenu, menu);
//        return true;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {


            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Called when the Traffic checkbox is clicked.
     */
    public void onTrafficToggled(View view) {
        updateTraffic();
    }

    private void updateTraffic() {
        if (!checkReady()) {
            return;
        }
        googleMap.setTrafficEnabled(mTrafficCheckbox.isChecked());
    }
    private boolean checkReady() {
        if (googleMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Called when the imbStartRide button is clicked.
     */
    public void onStartRide(View view) {
        Log.i("onStartRide:", "SendStartMail");
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        String subject = getString(R.string.subjectText);

        String language = Locale.getDefault().getLanguage();

        Log.i("onStartRide.language:", language);
        String htmlEmail = "";

        if (language.equals("es") ) {
            htmlEmail = "<body><h1>Estimado usuario,</h1><p>Esta es la Info de Inicio del Viaje :<br><br>\n" +
                    "      <strong>Fecha Viaje: "+ date +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>ID Conductor: "+ mDriverId +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Nombre Conductor: "+ mName +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Compañia: "+ mCompany +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Latitud: "+ latStart +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Longitud: "+ lngStart +" </strong> &nbsp;</p><br>\n" +
                    "      <blockquote>Saludos,<a></blockquote><br>Equipo VeelTaxi</body>";
        } else {
            htmlEmail = "<body><h1>Hi All,</h1><p>Here you Ride Start details Info :<br><br>\n" +
                    "      <strong>Ride Date: "+ date +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Taxi Driver ID: "+ mDriverId +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Taxi Driver Name: "+ mName +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Company: "+ mCompany +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Latitude: "+ latStart +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Longitude: "+ lngStart +" </strong> &nbsp;</p><br>\n" +
                    "      <blockquote>Greets,<a></blockquote><br>VeelTaxi Team</body>";
        }


        //String message = Html.fromHtml(getString(R.string.htmlEmail)).toString();
        String message = Html.fromHtml(htmlEmail).toString();
        //String message = htmlEmail;
        sendMail(email1, email2, email3, email4, email5, subject, message);
        imbStartRide.setEnabled(false);
        //imbStartRide.setTextColor(727272);
    }
    /**
     * Called when the imbEndRide button is clicked.
     */
    public void onEndRide(View view) {
        String latEnd ="";
        String lngEnd ="";
        // Getting Current Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this,"GPS Localization error" , Toast.LENGTH_SHORT).show();
            return;
        } else {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null){
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                latEnd = String.valueOf(latitude);
                lngEnd = String.valueOf(longitude);
                String msj = "lon:"+lngEnd+"-lat:"+latEnd;
                //Toast.makeText(this,msj , Toast.LENGTH_SHORT).show();

                Log.i("MapsActivity-end:", msj);
                //Send Mail with LatLng
                if(location!=null){
                    onLocationChanged(location);
                }
                locationManager.requestLocationUpdates(provider, 20000, 0, this);

            } else{
                Toast.makeText(this,"GPS Localization error" , Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Log.i("onEndRide:", "SendEndMail");
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        String subject = getString(R.string.subjectText);
        String language = Locale.getDefault().getLanguage();

        Log.i("onStartRide.language:", language);
        String htmlEmailEnd = "";

        if (language.equals("es") ) {
            htmlEmailEnd = "<body><h1>Estimado usuario,</h1><p>Esta es la Info de Fin del Viaje :<br><br>\n" +
                    "      <strong>Fecha Viaje: "+ date +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>ID Conductor: "+ mDriverId +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Nombre Conductor: "+ mName +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Compañia: "+ mCompany +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Latitud: "+ latEnd +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Longitud: "+ lngEnd +" </strong> &nbsp;</p><br>\n" +
                    "      <blockquote>Saludos,<a></blockquote><br>Equipo VeelTaxi</body>";
        } else {
            htmlEmailEnd = "<body><h1>Hi All,</h1><p>Here you Ride End details Info :<br><br>\n" +
                    "      <strong>Ride Date: "+ date +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Taxi Driver ID: "+ mDriverId +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Taxi Driver Name: "+ mName +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Company: "+ mCompany +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Latitude: "+ latEnd +" </strong> &nbsp;</p><br>\n" +
                    "      <strong>Longitude: "+ lngEnd +" </strong> &nbsp;</p><br>\n" +
                    "      <blockquote>Greets,<a></blockquote><br>VeelTaxi Team</body>";
        }

        String message = Html.fromHtml(htmlEmailEnd).toString();
        sendMail(email1, email2, email3, email4, email5, subject, message);
        imbEndRide.setEnabled(false);
        //tvwEndRide.setTextColor(727272);
    }

    private void sendMail(String email1, String email2,String email3, String email4, String email5,String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email1, email2,email3, email4, email5,subject, messageBody, session);
            new SendMailTask().execute(message);
            M.showToast(MapsActivity.this, getString(R.string.EmailSent));
        } catch (AddressException e) {
            M.showToast(MapsActivity.this, e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e1) {
            M.showToast(MapsActivity.this, e1.getMessage());
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            M.showToast(MapsActivity.this, e2.getMessage());
            e2.printStackTrace();
        }
    }

    private Message createMessage(String email1, String email2,String email3, String email4, String email5, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(getString(R.string.VeelTaxiEmail), getString(R.string.VeelTaxiAdmin)));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email1, email1));
        if (email2 == null) {
            email2 = "";
            M.showToast(MapsActivity.this, getString(R.string.ContactEmailDoesNotExist)+"-1");
        }else{
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email2, email2));
        }
        if (email3 == null) {
            email3 = "";
            M.showToast(MapsActivity.this, getString(R.string.ContactEmailDoesNotExist)+"-2");
        }else{
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email3, email3));
        }
        if (email4 == null) {
            email4 = "";
            M.showToast(MapsActivity.this, getString(R.string.ContactEmailDoesNotExist)+"-3");
        }else{
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email4, email4));
        }
        if (email5 == null) {
            email5 = "";
            M.showToast(MapsActivity.this, getString(R.string.ContactEmailDoesNotExist)+"-4");
        }else{
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email5, email5));
        }
        message.setSubject(subject);
        message.setText(messageBody);
        message.setHeader("dd", getString(R.string.messageHeader));

        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AllConstants.username, AllConstants.password);
            }
        });
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MapsActivity.this, getString(R.string.PleaseWait), getString(R.string.SendingMail), true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}