package com.sourcey.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcey.android.customerrorcode.Errors;
import com.sourcey.android.entity.Result;
import com.sourcey.android.utility.Constants;
import com.sourcey.android.utility.EndPointApi;
import com.sourcey.android.utility.HandleResult;
import com.sourcey.android.utility.Notifications;
import com.sourcey.android.utility.Utility;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private ObjectMapper mapper = new ObjectMapper();

    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        isOnline();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) goToActiviry(SignupActivity.class,null);
            }
        });
    }

    public void login() {
        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();
        if (!validate(username,password)) {
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(Notifications.PROCESSING.getMessage());
        progressDialog.show();

        new android.os.Handler().postDelayed(
                () -> {
                    if(handleLogin(username,password))
                        onLoginSuccess();
                    else
                        onLoginFailed();
                    progressDialog.dismiss();
                }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    private void initializeData(){
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        if(Utility.isValid(username)) _usernameText.setText(username);
        if(Utility.isValid(password)) _passwordText.setText(password);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Utility.notify(getBaseContext(),Notifications.LOGIN_SUCCESS.getMessage());
        goToActiviry(MainActivity.class,null);
    }

    public void onLoginFailed() {
        Utility.notify(getBaseContext(),Notifications.LOGIN_FAILED.getMessage());
        _loginButton.setEnabled(true);
    }

    private boolean handleLogin(String username,String password){
        ResponseExtractor<Result> extractor = new ResponseExtractor<Result>() {
            @Override
            public Result extractData(ClientHttpResponse response) throws IOException {
                HttpHeaders headers = response.getHeaders();
                return mapper.readValue(headers.getAuthorization(),Result.class);
            }
        };
        try {
            String url = String.format("%s?username=%s&password=%s",EndPointApi.LOGIN.getLink(),username,password);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Result result = restTemplate.execute(url, HttpMethod.GET, Utility.requestCallback, extractor);
            if(result.getCode().equals(Errors.SUCCESS.getId())){
                Utility.Bearer = result.getData().toString();
                return true;
            }else if(result.getCode().equals(Errors.USER_NOT_ACTIVATED.getId())){
                Utility.notify(getBaseContext(),HandleResult.ProcessCode(result.getCode()));
                goToActiviry(VerifyActivity.class,null);
                return false;
            }else{
                Utility.notify(getBaseContext(),HandleResult.ProcessCode(result.getCode()));
                return false;
            }
        } catch (Exception e) {
            Utility.notify(getBaseContext(),e.getMessage());
            return false;
        }
    }

    public boolean validate(String username,String password) {
        boolean valid = true;
        if (username.isEmpty()) {
            _usernameText.setError(Notifications.USERNAME_IS_NOT_EMPTY.getMessage());
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError(Notifications.PASSWORD_IS_NOT_EMPTY.getMessage());
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    private void goToActiviry(Class<?> cls, Map<String,String> maps){
        final Intent intent = new Intent(getApplicationContext(), cls);
        if(maps!=null)
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                intent.putExtra(entry.getKey(),entry.getValue());
            }
        startActivityForResult(intent, Constants.ZERO);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()) return true;
        Utility.notify(getBaseContext(),Notifications.NOT_INTERNET.getMessage());
        return false;

    }
}
