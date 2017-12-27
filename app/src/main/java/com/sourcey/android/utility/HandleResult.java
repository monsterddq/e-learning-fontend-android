package com.sourcey.android.utility;

import com.sourcey.android.customerrorcode.Errors;

/**
 * Created by Doan Quoc Thai on 12/27/2017.
 */

public class HandleResult {
    public static String ProcessCode(String code){
        StringBuilder result = new StringBuilder();
        for (Errors errors : Errors.values()) {
            if(errors.getId().equals(code)){
                result.append(errors.getMessage());
                break;
            }
        }
        return result.toString();
    }
}
