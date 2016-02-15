package app.com.ecandle.veeltaxi.webservices;


import app.com.ecandle.veeltaxi.model.DriverModel;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Jtomaylla on 2016-02-09.
 */
public interface DriverAPI {
@FormUrlEncoded
    @POST("/getDriverByDriverId.php")
    void search(@Field("driverid") String driverid,
               Callback<DriverModel> response);


//    @Multipart
//    @POST("/register")
//    void register(@Part("image") TypedFile image,
//                  @Part("username") String username,
//                  @Part("password") String Password,
//                  @Part("email") String email,
//                  @Part("countryid") String countryid,
//                  @Part("logintype") String logintype,
//                  Callback<ResponseModel> response);
//
//
//    @Multipart
//    @POST("/register")
//    void fbregister(@Part("username") String username,
//                    @Part("password") String Password,
//                    @Part("email") String email,
//                    @Part("countryid") String countryid,
//                    @Part("logintype") String logintype,
//                    @Part("name") String name,
//                    Callback<ResponseModel> response);
}
