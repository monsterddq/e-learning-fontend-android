package com.sourcey.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sourcey.android.customerrorcode.Errors;
import com.sourcey.android.entity.Result;
import com.sourcey.android.entity.UserSignup;
import com.sourcey.android.utility.Constants;
import com.sourcey.android.utility.EndPointApi;
import com.sourcey.android.utility.HandleResult;
import com.sourcey.android.utility.Notifications;
import com.sourcey.android.utility.Utility;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private Map<String,String> MAP = null;

    @Bind(R.id.input_username) EditText _usernnameText;
    @Bind(R.id.input_display_name) EditText _displayNameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_phone) EditText _phoneText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        String name = _usernnameText.getText().toString();
        String address = _displayNameText.getText().toString();
        String email = _emailText.getText().toString();
        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        final UserSignup userSignup = new UserSignup(name,password,email,address,phone);
        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(Notifications.PROCESSING.getMessage());
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(handleSignup(userSignup)){
                            onSignupSuccess();
                        }else {
                            onSignupFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 2000);

    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Utility.notify(getBaseContext(), Notifications.SIGNUP_SUCCESS.getMessage());
        MAP = new HashMap<>();
        MAP.put("email",_emailText.getText().toString());
        MAP.put("password",_passwordText.getText().toString());
        MAP.put("username",_usernnameText.getText().toString());
        goToActiviry(VerifyActivity.class,MAP);
    }

    public void onSignupFailed() {
        Utility.notify(getBaseContext(), Notifications.SIGNUP_FAILED.getMessage());
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _usernnameText.getText().toString();
        String displayName = _displayNameText.getText().toString();
        String email = _emailText.getText().toString();
        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty()) {
            _usernnameText.setError(Notifications.USERNAME_IS_NOT_EMPTY.getMessage());
            valid = false;
        } else {
            _usernnameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(Notifications.EMAIL_IS_INCORRECT.getMessage());
            valid = false;
        } else {
            _emailText.setError(null);
        }


        if (!phone.isEmpty() && phone.length()!=10 && Patterns.PHONE.matcher(phone).matches()) {
            _phoneText.setError(Notifications.PHONE_IS_INCORRECT.getMessage());
            valid = false;
        } else{ _phoneText.setError(null);}

        if (displayName.isEmpty()) {
            _displayNameText.setError(Notifications.DISPLAYNAME_IS_NOT_EMPTY.getMessage());
            valid = false;
        } else {_displayNameText.setError(null);}

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(Notifications.PASSWORD_TOO_LESS.getMessage());
            valid = false;
        } else {_passwordText.setError(null);}

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError(Notifications.PASSWORD_DONT_MATH.getMessage());
            valid = false;
        } else {_reEnterPasswordText.setError(null);}

        return valid;
    }

    public boolean handleSignup(UserSignup userSignup){
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Result result = restTemplate.postForObject(EndPointApi.SINGUP.getLink(), userSignup, Result.class);
            if(result.getCode().equals(Errors.SUCCESS.getId())) return true;
            Utility.notify(getBaseContext(),HandleResult.ProcessCode(result.getCode()));
            return false;
        }catch (Exception e){
            Utility.notify(getBaseContext(),e.getMessage());
            return false;
        }
    }

    private void goToActiviry(Class<?> cls, Map<String,String> maps){
        final Intent intent = new Intent(getApplicationContext(), cls);
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            intent.putExtra(entry.getKey(),entry.getValue());
        }
        startActivityForResult(intent, Constants.ZERO);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}