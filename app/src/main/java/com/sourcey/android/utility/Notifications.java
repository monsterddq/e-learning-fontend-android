package com.sourcey.android.utility;

/**
 * Created by Doan Quoc Thai on 12/27/2017.
 */

public enum Notifications {

    NOT_INTERNET("019","Not Internet"),
    USER_ACTIVATED("018","User activated"),
    ACTIVATE_SUCCESS("017","Activate Success"),
    LOGIN_SUCCESS("001","Login Success"),
    USERNAME_IS_NOT_EMPTY("002","Username is not empty"),
    PASSWORD_IS_NOT_EMPTY("003","Password is not empty"),
    USERNAME_OR_PASSWORD_IS_NOT_MATHC("004","Username or password is not match"),
    LOGIN_FAILED("005","Login Failed"),
    EMAIL_IS_INCORRECT("007","Email is incorrect"),
    PHONE_IS_INCORRECT("008","Phone is incorrect"),
    PASSWORD_TOO_LESS("009","Password is between 4 and 10 characters"),
    PASSWORD_DONT_MATH("010","Password don't match"),
    DISPLAYNAME_IS_NOT_EMPTY("013","Display name is not empty"),
    SIGNUP_FAILED("011","Signup Failed"),
    SIGNUP_SUCCESS("012","Signup Success"),
    ACTIVATION_DIGEST_IS_NOT_EMPTY("014","Activation Digest is not empty"),
    VERIFY_FAILED("015","Verify Failed"),
    VERIFY_SUCCESS("016","Verify Success"),
    PROCESSING("006","Processing ...");

    private String id;
    private String message;

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    Notifications() {
    }

    Notifications(String id, String message) {
        this.id = id;
        this.message = message;
    }
}
