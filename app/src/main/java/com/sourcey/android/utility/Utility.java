package com.sourcey.android.utility;

import android.content.Context;
import android.widget.Toast;

import com.sourcey.android.config.XUserAgentInterceptor;
import com.sourcey.android.entity.CategoryDto;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Doan Quoc Thai on 12/27/2017.
 */

public class  Utility {
    public static String Bearer = "";
    public static RequestCallback requestCallback = new RequestCallback() {
        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            request.getHeaders().add(Constants.HEADER_AUTHORIZATION,Utility.Bearer);
        }
    };
    public static RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setInterceptors(Collections.singletonList(new XUserAgentInterceptor()));
        return restTemplate;
    }
    public static void notify(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static boolean isValid(String value){
        return value!=null && !value.isEmpty();
    }
    public static List<CategoryDto> categoryDtoList = new ArrayList<>();
}
