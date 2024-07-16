package com.example.adsphone_app.accountEmail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adsphone_app.activity.MainActivity;
import com.example.adsphone_app.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adsphone_app.activity.Manhinhcho;
import com.example.adsphone_app.otpphoner.LoginPhoneNumber;
import com.example.adsphone_app.retrofit.ApiBanHang;
import com.example.adsphone_app.retrofit.RetrofitInstance;
import com.example.adsphone_app.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Login_Page extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button signIn;

    TextView signUp, textOTP;
    ImageView backBtn;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        anhxa();
        initControll();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, Manhinhcho.class);
                startActivity(intent);
                finish();
            }
        });

        textOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, LoginPhoneNumber.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initControll() {
        //Đăng kí
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, Register_Page.class);
                startActivity(intent);
                finish();
            }
        });

        //Đăng nhập
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = editTextEmail.getText().toString().trim();
                String str_pass = editTextPassword.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập Mật khẩu", Toast.LENGTH_SHORT).show();
                }else {
                    //luu tai khoan
                    Paper.book().write("email", str_email);
                    Paper.book().write("pass", str_pass);
                    compositeDisposable.add(apiBanHang.dangnhap(str_email,str_pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if (userModel.isSuccess()){
                                            Utils.user_current = userModel.getResult().get(0);
                                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                }
            }
        });
    }
    private void anhxa() {
        Paper.init(this);
        apiBanHang = RetrofitInstance.getRetrofitInstance(Utils.BASE_URL).create(ApiBanHang.class);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        textOTP = findViewById(R.id.textOTP);
        backBtn = findViewById(R.id.backBtn);

        //đọc data
        if (Paper.book().read("email") != null && Paper.book().read("pass") != null){
            editTextEmail.setText(Paper.book().read("email"));
            editTextPassword.setText(Paper.book().read("pass"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() !=null){
            editTextEmail.setText(Utils.user_current.getEmail());
            editTextPassword.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}