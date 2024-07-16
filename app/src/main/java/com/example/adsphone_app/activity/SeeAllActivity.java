package com.example.adsphone_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.adsphone_app.R;
import com.example.adsphone_app.adapter.SPMoiAdapter;
import com.example.adsphone_app.model.SanPhamMoi;
import com.example.adsphone_app.retrofit.ApiBanHang;
import com.example.adsphone_app.retrofit.RetrofitInstance;
import com.example.adsphone_app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SeeAllActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SPMoiAdapter spMoiAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        apiBanHang = RetrofitInstance.getRetrofitInstance(Utils.BASE_URL).create(ApiBanHang.class);
        anhxa();
        sanpham();
        toobar();
    }

    private void toobar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sanpham() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spMoiAdapter = new SPMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyclerView.setAdapter(spMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không có kết nối mạng, vui lòng kết nối"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void anhxa() {
        toolbar = findViewById(R.id.toobar_SeeAll);
        recyclerView = findViewById(R.id.recycleviewSeeAll);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //khoi tao list
        mangSpMoi = new ArrayList<>();
    }
}