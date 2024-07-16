package com.example.adsphone_app.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.adsphone_app.R;
import com.example.adsphone_app.adapter.LoaispAdapter;

import com.example.adsphone_app.adapter.SPMoiAdapter;
import com.example.adsphone_app.model.Item;
import com.example.adsphone_app.model.Loaisp;
import com.example.adsphone_app.model.SanPhamMoi;
import com.example.adsphone_app.retrofit.ApiBanHang;
import com.example.adsphone_app.retrofit.RetrofitInstance;
import com.example.adsphone_app.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
        Toolbar toolbar;
        ViewFlipper viewFlipper;
        RecyclerView recyclerView;
        List<Loaisp> mangloaiSp;
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        ApiBanHang apiBanHang;
        List<SanPhamMoi> mangSpMoi;
        SPMoiAdapter spMoiAdapter;
        NotificationBadge badge;
        FrameLayout frameLayout;
        ImageView imgsearch;
        TextView seeAll;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            apiBanHang = RetrofitInstance.getRetrofitInstance(Utils.BASE_URL).create(ApiBanHang.class);
            anhxa();
            botton_navigation();
            manhinhqc();
            getSanPhamMoi();
        }

    private void botton_navigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout adminBtn = findViewById(R.id.adminBtn);
        LinearLayout puma = findViewById(R.id.click_puma);
        LinearLayout donhang = findViewById(R.id.donhangBtn);
        LinearLayout adidas = findViewById(R.id.click_adidas);
        LinearLayout dep = findViewById(R.id.click_dep);
        LinearLayout nike = findViewById(R.id.click_nike);
        LinearLayout dabong = findViewById(R.id.click_giay_dabong);

        donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DonHangActivity.class);
                startActivity(intent);
            }
        });

        homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));


        adminBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
        });

//        yeuthichBtn.setOnClickListener(v -> {
//            startActivity(new Intent(MainActivity.this, .class));
//        });

        adidas.setOnClickListener(v -> {
            Intent giayadidas = new Intent(getApplicationContext(), GiayAdidasActivity.class);
            giayadidas.putExtra("loai", 1);
            startActivity(giayadidas);
        });

        puma.setOnClickListener(v -> {
            Intent giaypuma = new Intent(getApplicationContext(), GiayPumaActivity.class);
            giaypuma.putExtra("loai", 2);
            startActivity(giaypuma);
        });

        dabong.setOnClickListener(v -> {
            Intent dabongg = new Intent(getApplicationContext(), GiayDaBongActivity.class);
            dabongg.putExtra("loai", 4);
            startActivity(dabongg);
        });


        dep.setOnClickListener(v -> {
            Intent depactivity = new Intent(getApplicationContext(), DepActivity.class);
            depactivity.putExtra("loai", 3);
            startActivity(depactivity);
        });

        nike.setOnClickListener(v -> {
            Intent nikeactivity = new Intent(getApplicationContext(), GiayNikeActivity.class);
            nikeactivity.putExtra("loai", 5);
            startActivity(nikeactivity);
        });

    }

    private void getSanPhamMoi() {
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

        private void manhinhqc() {
            int[] imageResources = {R.drawable.bia1, R.drawable.bia2, R.drawable.bia3};

            for (int i = 0; i < imageResources.length; i++) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(imageResources[i]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewFlipper.addView(imageView);
            }

            viewFlipper.setFlipInterval(3000);
            viewFlipper.setAutoStart(true);

            Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_night);
            Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

            viewFlipper.setInAnimation(fadeIn);
            viewFlipper.setOutAnimation(fadeOut);
        }

        private void anhxa() {
            toolbar = findViewById(R.id.toolbarmanhinh);
            viewFlipper = findViewById(R.id.viewLilipper);
            recyclerView = findViewById(R.id.recycleview);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            badge = findViewById(R.id.menu_sl);
            frameLayout = findViewById(R.id.framegiohang);
            imgsearch = findViewById(R.id.img_search);
            //khoi tao list
            mangloaiSp = new ArrayList<>();
            mangSpMoi = new ArrayList<>();
            if (Paper.book().read("giohang") != null){
                Utils.manggiohang = Paper.book().read("giohang");
            }
            //
            seeAll = findViewById(R.id.seeAll);
            if (Utils.manggiohang == null){
                Utils.manggiohang = new ArrayList<>();
            }else {
                int totalItem = 0;
                for (int i = 0; i<Utils.manggiohang.size(); i++){
                    totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
                }
                badge.setText(String.valueOf(totalItem));
            }
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                    startActivity(intent);
                }
            });

            //tim kiem sp
            imgsearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                }
            });
            //
            seeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SeeAllActivity.class);
                    startActivity(intent);
                }
            });
        }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i = 0; i<Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

}