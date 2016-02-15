package app.com.ecandle.veeltaxi.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
 
    private Context _context;
 
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              //NetworkInfo[] info = connectivity.ge.getAllNetworkInfo();
              NetworkInfo info = connectivity.getActiveNetworkInfo();
              if (info != null)
//                  for (int i = 0; i < info.length; i++)
//                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
//                      {
//                          return true;
//                      }
              if (info.getState() == NetworkInfo.State.CONNECTED){
                  return true;
              }
 
          }
          return false;
    }
}
