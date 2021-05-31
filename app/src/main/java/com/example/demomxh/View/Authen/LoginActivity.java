package com.example.demomxh.View.Authen;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.SendformLogin;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;
import com.example.demomxh.View.MainScreen.MainScreenActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.edt_log_in_username)
    EditText edtUsername;
    @BindView(R.id.edt_log_in_password)
    EditText edtPassword;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.imv_hide_show_pass)
    ImageView imvHideShowPass;
    @BindView(R.id.btn_go_to_register)
    Button btnGoToRegister;

    private int hidePass = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();
        RegisterEvent();
    }

    private void RegisterEvent() {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if(username.isEmpty() && password.isEmpty()){
                    ShowToast("Username or password mustn't be blank!");
                }else{
                    SendformLogin sendformLogin = new SendformLogin(username,password);
                    Login(sendformLogin);
                    Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                    startActivity(intent);
                }
            }
        });

        imvHideShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hidePass == 0){
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    hidePass = 1;
                }else{
                    edtPassword.setTransformationMethod(null);
                    hidePass = 0;
                }
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login(SendformLogin sendformLogin) {
        retrofitService.login(sendformLogin).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.code()==200){
                    UserInfo userInfo = response.body();
                    realmContext.addUser(userInfo);
                    Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ShowToast("Login Sucessfully");
                    Log.d("TESTLOGIN",realmContext.getUser().getUserId());
                }
                else {
                    Log.d("testLogin","responsecode != 200");
                    ShowToast("Login unsucessfully");
                }
            }
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                ShowToast("Unsucessfully");
            }
        });
    }

    private void Init() {
        ButterKnife.bind(this);
    }
}