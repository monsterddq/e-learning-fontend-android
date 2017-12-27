package com.sourcey.android.entity;


import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Doan Quoc Thai on 12/27/2017.
 */
@Data
@NoArgsConstructor
public class VerifyDto {
    private String email;
    private String activationDigest;
    private Date activatedAt;

    public VerifyDto(String email, String activationDigest) {
        this.email = email;
        this.activationDigest = activationDigest;
        this.activatedAt = new Date();
    }
}
