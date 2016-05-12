package edu.sjsu.cmpe277.rentalapp.pojo;

import android.app.Application;

/**
 * Created by ram.mandadapu on 5/8/16.
 */
public class GlobalPojo extends Application{

    private String userName;
    private String email;
    private String imageUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
