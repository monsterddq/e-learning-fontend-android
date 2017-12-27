package com.sourcey.android.entity;

import java.util.Date;


public class UserDto {

    private String username;

    private String password;

    private String activationDigest;

    private Date activatedAt;

    private String rememberDigest;

    private String resetDigest;

    private Date resetSentAt;

    private String displayName;

    private int activated;

    private String email;

    private String phone;
    private String address;
    private String avatar;

    private String role;
    private Date createdAt;

    private Date updatedAt;

    public UserDto(String username) {
        this.username = username;
    }

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDto(String username, String displayName, String email, String phone, String address, String avatar, String role) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.role = role;
    }
}
