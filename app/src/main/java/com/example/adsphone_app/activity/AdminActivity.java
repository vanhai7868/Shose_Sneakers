package com.example.adsphone_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.adsphone_app.R;
import com.example.adsphone_app.accountEmail.Login_Page;
import com.example.adsphone_app.adapter.LoaispAdapter;
import com.example.adsphone_app.model.Loaisp;
import com.example.adsphone_app.retrofit.ApiBanHang;
import com.example.adsphone_app.retrofit.RetrofitInstance;
import com.example.adsphone_app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminActivity extends AppCompatActivity {
    Toolbar toolbar;
    LoaispAdapter loaispAdapter;
    List<Loaisp> mangloaisp;
    ListView listView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        apiBanHang = RetrofitInstance.getRetrofitInstance(Utils.BASE_URL).create(ApiBanHang.class);
        anhxa();
        getEventClick();
        ToolbarAdmin();
        username();
        if (isConnected(this)){
            getLoaisp();
        }else {
            Toast.makeText(getApplicationContext(), "ko co internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void username() {
        user.setText(Utils.user_current.getMobile());
    }

    private void getEventClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent thongtin = new Intent(getApplicationContext(), ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                    case 1:
                        Intent lienhe = new Intent(getApplicationContext(), LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                    case 2:
                        Intent donhang = new Intent(getApplicationContext(), DonHangActivity.class);
                        startActivity(donhang);
                        break;
                    case 3:
                        Intent dangxuat = new Intent(getApplicationContext(), Login_Page.class);
                        startActivity(dangxuat);
                        break;
                }
            }
        });
    }

    private void getLoaisp() {
        compositeDisposable.add(apiBanHang.getLoaisp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()){
                                mangloaisp = loaiSpModel.getResult();
                                loaispAdapter = new LoaispAdapter(getApplicationContext(), mangloaisp);
                                listView.setAdapter(loaispAdapter);
                            }
                        }
                ));
    }

    private void ToolbarAdmin() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar = findViewById(R.id.your_toolbar_id);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.listviewmanhinh);
        mangloaisp = new ArrayList<>();
        user = findViewById(R.id.username_admin);
    }
    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}