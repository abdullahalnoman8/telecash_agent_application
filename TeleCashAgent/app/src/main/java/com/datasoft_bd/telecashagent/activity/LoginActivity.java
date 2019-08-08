package com.datasoft_bd.telecashagent.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.datasoft_bd.telecashagent.R;
import com.datasoft_bd.telecashagent.service.ApiClient;
import com.datasoft_bd.telecashagent.service.WebConnectionApi;
import com.datasoft_bd.telecashagent.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.et_phone)
    EditText phone;
    @BindView(R.id.et_password)
    EditText password;
    private ProgressDialog progressDialog;
    private WebConnectionApi webConnectionApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        //removing title and set full screen activity
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        webConnectionApi = ApiClient.getAuthClient(WebConnectionApi.AUTH_URL).create(WebConnectionApi.class);
    }

    @OnClick(R.id.btn_login)
    public void onLoginButtonClick() {
        if(phone.getText().toString().trim().equals("")){
            phone.setError("Please Enter your phone number here");
        }else if(password.getText().toString().trim().equals("")){
            password.setError("Enter your password here");
        }else{
            String userPhone = phone.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            Log.d("User Phone: ",userPhone);
            Log.d("Password",userPassword);
            login(userPhone,userPassword);
        }

    }

    private void login(String userPhone, String userPassword) {
        Utils.showProgressDialog(progressDialog, true);
        byte[] credentials = "client1:secret".getBytes();
        String authorization = "Basic "+ Base64.encodeToString(credentials,Base64.NO_WRAP);
        final Call<Object> getResponseData = webConnectionApi.accessToken("password", userPhone, userPassword, authorization);
        getResponseData.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Utils.dismissProgressDialog(progressDialog, false);
                Utils.showToast(getApplicationContext(), response.body().toString(), false);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Utils.dismissProgressDialog(progressDialog, false);
                Utils.showToast(getApplicationContext(), "Please Check Your Internet Connection", true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
