package com.sourcey.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sourcey.android.customerrorcode.Errors;
import com.sourcey.android.entity.Result;
import com.sourcey.android.entity.VerifyDto;
import com.sourcey.android.utility.Constants;
import com.sourcey.android.utility.EndPointApi;
import com.sourcey.android.utility.HandleResult;
import com.sourcey.android.utility.Notifications;
import com.sourcey.android.utility.Utility;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VerifyActivity extends AppCompatActivity {
    private static final String TAG = "VeifyActivify";
    private Map<String,String> MAP = null;
    private String username = "";
    private String password = "";

    @Bind(R.id.input_activationDigest) EditText _activatedDigest;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.btn_verify) Button _btnVerify;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        String email = getIntent().getStringExtra("email");
        initializeData(email);

        _loginLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                goToLoginActivity(username,password);
            }
        });
        _btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
    }

    private void verify() {
        String email = _emailText.getText().toString();
        String activationDigest = _activatedDigest.getText().toString();
        if(!validate()){
            onVerifyFailed();
            return;
        }
        _btnVerify.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(VerifyActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(Notifications.PROCESSING.getMessage());
        progressDialog.show();

        final VerifyDto verifyDto = new VerifyDto(email,activationDigest);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(handleVerify(verifyDto))
                            onVerifySuccess();
                        else
                            onVerifyFailed();
                        progressDialog.dismiss();
                    }
                }, 500);
    }

    private void onVerifySuccess() {
        Utility.notify(getBaseContext(),Notifications.VERIFY_SUCCESS.getMessage());
        goToLoginActivity(username,password);
        finish();
    }

    private void onVerifyFailed() {
        _btnVerify.setEnabled(true);
        Utility.notify(getBaseContext(),Notifications.VERIFY_FAILED.getMessage());
    }

    private boolean handleVerify(VerifyDto verifyDto) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Result result = restTemplate.postForObject(EndPointApi.VERIFY.getLink(), verifyDto, Result.class);
            if(result.getCode().equals(Errors.SUCCESS.getId())){
                Utility.notify(getBaseContext(),Notifications.ACTIVATE_SUCCESS.getMessage());
                return true;
            }else if(result.getCode().equals(Errors.USER_ACTIVATED.getId())){
                Utility.notify(getBaseContext(),Notifications.ACTIVATE_SUCCESS.getMessage());
                return true;
            }
            else{
                Utility.notify(getBaseContext(),HandleResult.ProcessCode(result.getCode()));
                return false;
            }
        }catch (Exception e){
            Utility.notify(getBaseContext(),e.getMessage());
            return false;
        }
    }


    private boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String activationDigest = _activatedDigest.getText().toString();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailText.setError(Notifications.EMAIL_IS_INCORRECT.getMessage());
            valid = false;
        }else _emailText.setError(null);
        if(activationDigest.isEmpty()){
            _activatedDigest.setError(Notifications.ACTIVATION_DIGEST_IS_NOT_EMPTY.getMessage());
            valid = false;
        }else _activatedDigest.setError(null);
        return valid;
    }

    private void initializeData(String email){
        if(Utility.isValid(email)) _emailText.setText(email,TextView.BufferType.EDITABLE);
    }

    private void goToLoginActivity(String username,String password){
        MAP = new HashMap<>();
        MAP.put("username", !Utility.isValid(username) ? Constants.EMPTY : username );
        MAP.put("password", !Utility.isValid(password) ? Constants.EMPTY : password );
        goToActivity(LoginActivity.class,MAP);
    }

    private void goToActivity(Class<?> cls, Map<String,String> maps){
        final Intent intent = new Intent(getApplicationContext(), cls);
        if(maps!=null)
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                intent.putExtra(entry.getKey(),entry.getValue());
            }
        startActivityForResult(intent, Constants.ZERO);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
