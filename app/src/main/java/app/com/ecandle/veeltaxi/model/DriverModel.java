package app.com.ecandle.veeltaxi.model;

/**
 * Created by jtomaylla on 2016-02-09.
 */
public class DriverModel {


    String driver_id;
    String document_id;
    String license_id;
    String photo_driver_front;
    String first_name;
    String last_name;
    String email;
    String phone;
    String address;
    String company_name;
    String success;
    String message;


    public DriverModel(String driver_id, String document_id, String license_id, String photo_driver_front, String first_name, String last_name, String email, String phone, String address,String company_name, String success, String message) {
        this.driver_id = driver_id;
        this.document_id = document_id;
        this.license_id = license_id;
        this.photo_driver_front = photo_driver_front;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.company_name = company_name;
        this.success = success;
        this.message = message;

    }


    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public String getLicense_id() {
        return license_id;
    }

    public void setLicense_id(String license_id) {
        this.license_id = license_id;
    }

    public String getPhoto_driver_front() {
        return photo_driver_front;
    }

    public void setPhoto_driver_front(String photo_driver_front) {
        this.photo_driver_front = photo_driver_front;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCompany_name() {return company_name;}

    public void setCompany_name(String company_name) { this.company_name = company_name;}


    @Override
    public String toString() {
        return "DriverModel{" +
                " driver_id='" + driver_id + '\'' +
                ", document_id='" + document_id + '\'' +
                ", license_id='" + license_id + '\'' +
                ", photo_driver_front='" + photo_driver_front + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", company_name='" + company_name + '\'' +
                '}';
    }

}