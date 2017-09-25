package com.foodie.app.build.api;

/**
 * Created by Tamil on 9/25/2017.
 */

public class APIError {
    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
