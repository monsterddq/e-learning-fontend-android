package com.sourcey.android.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sourcey.android.utility.CustomDateAndTimeDeserialize;

import java.util.Date;

/**
 * Created by Doan Quoc Thai on 12/27/2017.
 */

public class UserSignup {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String displayName;
    @JsonDeserialize(using=CustomDateAndTimeDeserialize.class)
    private Date createdAt;
    @JsonDeserialize(using=CustomDateAndTimeDeserialize.class)
    private Date updatedAt;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserSignup() {
    }

    public UserSignup(String username, String password, String email, String displayName, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.displayName = displayName;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }


}
