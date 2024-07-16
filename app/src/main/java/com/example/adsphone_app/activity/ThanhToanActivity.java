

package com.example.adsphone_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adsphone_app.R;
import com.example.adsphone_app.model.GioHang;
import com.example.adsphone_app.retrofit.ApiBanHang;
import com.example.adsphone_app.retrofit.RetrofitInstance;
import com.example.adsphone_app.utils.Utils;
import com.google.gson.Gson;
import android.util.Log;

import java.text.DecimalFormat;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail;
    EditText edtdiachi;
    Button btndathang;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    long tongtien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        
        anhxa();
        countItem();
        initControll();
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i<Utils.mangmuahang.size(); i++){
            totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initControll() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien)+" VND");
        txtemail.setText(Utils.user_current.getEmail());
        txtsodt.setText(Utils.user_current.getMobile());

        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else {
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sodt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();

                    Log.d("test", new Gson().toJson(Utils.mangmuahang));
                    try {
                        compositeDisposable.add(apiBanHang.createDber(str_email, str_sodt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        userModel -> {
                                            Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();

                                            //clear manggiohang chay qua mangmuahang
                                            for (int i=0; i<Utils.mangmuahang.size(); i++){
                                                GioHang gioHang = Utils.mangmuahang.get(i);
                                                if (Utils.manggiohang.contains(gioHang)){
                                                    Utils.manggiohang.remove(gioHang);
                                                }
                                            }
                                            Utils.mangmuahang.clear();
                                            Paper.book().write("giohang", Utils.manggiohang);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        },
                                        throwable -> {
                                            Log.e("ThanhToanActivity", "Error placing order", throwable);
                                            Toast.makeText(getApplicationContext(), "Đặt hàng thất bại. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle exception, show a toast, log, etc.
                    }

                }
            }
        });
    }

    private void anhxa() {
        apiBanHang = RetrofitInstance.getRetrofitInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobarthanhtoan);
        txttongtien = findViewById(R.id.txtTongtien);
        txtsodt = findViewById(R.id.txtSdt);
        txtemail = findViewById(R.id.txtEmail);
        edtdiachi = findViewById(R.id.edtDiachi);
        btndathang = findViewById(R.id.btndathang);
    }

    @Override
    protected void onResume() {
        compositeDisposable.clear();
        super.onResume();
    }
}