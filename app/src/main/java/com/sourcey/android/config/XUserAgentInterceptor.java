package com.sourcey.android.config;

import com.sourcey.android.utility.Constants;
import com.sourcey.android.utility.Utility;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Created by Doan Quoc Thai on 12/28/2017.
 */

public class XUserAgentInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(Constants.HEADER_AUTHORIZATION, Utility.Bearer);
        return execution.execute(request, body);
    }
}
