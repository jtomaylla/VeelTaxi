package app.com.ecandle.veeltaxi.model;

// *
// * Created by jtomaylla on 2016-02-02.
// *
public class LoginModel {


    String userid, success, message;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginModel(String userid, String success, String message) {
        this.userid = userid;
        this.success = success;

        this.message = message;
    }
}