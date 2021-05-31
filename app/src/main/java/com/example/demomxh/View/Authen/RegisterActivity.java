package com.example.demomxh.View.Authen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Model.SendformRegister;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edt_register_username)
    EditText edtUsername;
    @BindView(R.id.edt_register_password)
    EditText edtPass;
    @BindView(R.id.edt_register_email)
    EditText edtEmail;
    @BindView(R.id.edt_register_full_name)
    EditText edtName;
    @BindView(R.id.edt_register_phone)
    EditText edtPhone;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Init();
        RegisterEvent();
    }
    private void RegisterEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                if (username.isEmpty() || pass.isEmpty() || email.isEmpty() || name.isEmpty() || phone.isEmpty() || checkPass(pass) ){
                    ShowToast("Register unsucessfully.");
                }else{
                    SendformRegister sendformRegister = new SendformRegister(username,pass,email,phone,name);
                    Register(sendformRegister);
                }
            }
        });
    }

    private void Register(SendformRegister sendformRegister) {
        retrofitService.register(sendformRegister).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.code() == 200){
                    ShowToast("Register Sucessfully");
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    ShowToast(response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                ShowToast("Register unsucessfully");
            }
        });
    }

    private boolean checkPass(String pass){
        boolean capitalFlag = false;
        boolean lowerCaseFlag =false;
        boolean numberFlag = false;
        for (int i = 0; i < pass.length() ; i++) {
            char ch = pass.charAt(i);
            if(Character.isUpperCase(ch)){
                capitalFlag = true;
            }else if(Character.isDigit(ch)){
                numberFlag = true;
            }else if(Character.isLowerCase(ch)){
                lowerCaseFlag=true;
            }
            if (capitalFlag && numberFlag && lowerCaseFlag ){
                return true;
            }
        }
        return false;
    }

    private void Init() {
        ButterKnife.bind(this);
    }
}