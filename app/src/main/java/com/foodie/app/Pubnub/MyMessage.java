package com.foodie.app.Pubnub;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anjith Sasindran
 * on 11-Oct-15.
 */
public class MyMessage {

    @SerializedName("username")
    String username;
    @SerializedName("message")
    String message;

    public MyMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
