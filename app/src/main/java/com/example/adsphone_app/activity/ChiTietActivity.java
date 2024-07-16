package com.example.adsphone_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adsphone_app.R;
import com.example.adsphone_app.model.GioHang;
import com.example.adsphone_app.model.SanPhamMoi;
import com.example.adsphone_app.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

import io.paperdb.Paper;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp, giasp,mota;
    Button btnthem;
    ImageView imghinhanh;
    Spinner spinner, size;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        anhxa();
        ActionBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
                Paper.book().write("giohang", Utils.manggiohang);
            }
        });
    }
    private void themGioHang() {
        if (Utils.manggiohang.size() > 0) {
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            int soluong1 = Integer.parseInt(size.getSelectedItem().toString());

            // Kiểm tra sản phẩm đã tồn tại trong giỏ hàng chưa
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                if (Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()) {
                    // Sản phẩm đã tồn tại, cập nhật thông tin
                    updateCartItem(i, soluong, soluong1);
                    flag = true;
                    break;
                }
            }

            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm mới
            if (!flag) {
                addNewItemToCart(soluong, soluong1);
            }
        } else {
            // Giỏ hàng đang trống, thêm sản phẩm mới
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            int soluong1 = Integer.parseInt(size.getSelectedItem().toString());
            addNewItemToCart(soluong, soluong1);
        }

        // Cập nhật số lượng sản phẩm trên huy hiệu giỏ hàng
        int totalItem = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    // Phương thức cập nhật thông tin sản phẩm trong giỏ hàng
    private void updateCartItem(int position, int soluong, int soluong1) {
        GioHang gioHang = Utils.manggiohang.get(position);
        gioHang.setSoluong(soluong + gioHang.getSoluong());

        // Giữ nguyên giá trị của size
        gioHang.setSize(soluong1);

        long gia = Long.parseLong(sanPhamMoi.getGiasp()) * gioHang.getSoluong();
        gioHang.setGiasp(gia);
    }


    // Phương thức thêm sản phẩm mới vào giỏ hàng
    private void addNewItemToCart(int soluong, int soluong1) {
        long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soluong;
        GioHang gioHang = new GioHang();
        gioHang.setGiasp(gia);
        gioHang.setSoluong(soluong);
        gioHang.setSize(soluong1);
        gioHang.setIdsp(sanPhamMoi.getId());
        gioHang.setTensp(sanPhamMoi.getTensp());
        gioHang.setHinhsp(sanPhamMoi.getHinhanh());
        Utils.manggiohang.add(gioHang);
    }


    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText(decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+ " VND");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);

        Integer[] so1 = new Integer[]{35,36,37,38,39,40,41,42,43,44};
        ArrayAdapter<Integer> sizesp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so1);
        size.setAdapter(sizesp);
    }

    private void ActionBar() {
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
        tensp = findViewById(R.id.txttensp);
        giasp = findViewById(R.id.txtgiasp);
        mota = findViewById(R.id.txtmota);
        btnthem = findViewById(R.id.btnthemvaogio);
        imghinhanh = findViewById(R.id.imgchitiet);
        spinner = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toobar_Chitiet);
        badge = findViewById(R.id.menu_sl);
        size = findViewById(R.id.size_sp);
        FrameLayout frameLayout = findViewById(R.id.framegiohang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for (int i = 0; i<Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
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