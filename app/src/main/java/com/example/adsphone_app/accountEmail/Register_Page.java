package com.example.adsphone_app.accountEmail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adsphone_app.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adsphone_app.activity.Manhinhcho;
import com.example.adsphone_app.retrofit.ApiBanHang;
import com.example.adsphone_app.retrofit.RetrofitInstance;
import com.example.adsphone_app.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Register_Page extends AppCompatActivity {
    TextInputEditText email, pass, repass, mobile, username;
    Button signUp;

    TextView signIn;
    ImageView backBtn;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        anhxa();
        initControll();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Page.this, Manhinhcho.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initControll() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Page.this, Login_Page.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangki();
            }
        });
    }

    private void dangki() {
        String str_user = username.getText().toString().trim();
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
        } else if (!str_email.endsWith("@gmail.com")) {
            Toast.makeText(getApplicationContext(), "Email phải có đuôi là @gmail.com", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập Mật khẩu", Toast.LENGTH_SHORT).show();
        } else if (str_pass.length() < 6) {
            Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_repass)) {
            Toast.makeText(getApplicationContext(), "Không để trống nhập lại Mật khẩu", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_mobile)) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập Số điện thoại", Toast.LENGTH_SHORT).show();
        } else if (!isValidPhoneNumber(str_mobile)) {
            Toast.makeText(getApplicationContext(), "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_user)) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập Username", Toast.LENGTH_SHORT).show();
        } else {
            if (str_pass.equals(str_repass)) {
                compositeDisposable.add(apiBanHang.dangky(str_email,str_pass, str_user, str_mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()){
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setPass(str_pass);
                                        Toast.makeText(getApplicationContext(), "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login_Page.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            } else {
                Toast.makeText(getApplicationContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy kiểm tra định dạng số điện thoại
        String phoneRegex =  "^[0-9]{10,13}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phoneNumber).matches();
    }

    private void anhxa() {
        apiBanHang = RetrofitInstance.getRetrofitInstance(Utils.BASE_URL).create(ApiBanHang.class);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.Enterpassword);
        mobile = findViewById(R.id.mobile);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        backBtn = findViewById(R.id.backBtn);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}