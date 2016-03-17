package app.com.ecandle.veeltaxi.webservices;


import app.com.ecandle.veeltaxi.model.DriverModel;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

// **
// Created by Jtomaylla on 2016-02-09.
// **
public interface DriverAPI {
    @FormUrlEncoded
    @POST("/getDriverByDriverId.php")
    void search(@Field("driverid") String driverid, Callback<DriverModel> response);

}
