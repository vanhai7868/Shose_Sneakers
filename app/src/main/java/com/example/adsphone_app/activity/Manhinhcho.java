package com.example.adsphone_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adsphone_app.R;
import com.example.adsphone_app.accountEmail.Login_Page;
import com.example.adsphone_app.accountEmail.Register_Page;

public class Manhinhcho extends AppCompatActivity {

    Button btnDN;
    Button btnDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhcho);

        btnDN = findViewById(R.id.btnDN);
        btnDK = findViewById(R.id.btnDK);

        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Manhinhcho.this, Login_Page.class));
            }
        });

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manhinhcho.this, Register_Page.class);
                startActivity(intent);
            }
        });
    }
}