package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class UrunAyarlar extends AppCompatActivity {
    Button urunEkleBtn, urunSilBtn, urunGuncelleBtn,kategoriEkleBtn;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ayarlar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        urunEkleBtn = findViewById(R.id.urunEkle);
        urunSilBtn = findViewById(R.id.urunSil);
        urunGuncelleBtn = findViewById(R.id.urunGuncelle);
        kategoriEkleBtn = findViewById(R.id.kategoriEkle);

        kategoriEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UrunAyarlar.this, KategoriEkle.class));
            }
        });
        urunEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UrunAyarlar.this, UrunEkle.class));
            }
        });

        urunGuncelleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UrunAyarlar.this, UrunGuncelle.class));
            }
        });

        urunSilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UrunAyarlar.this, UrunSil.class));
            }
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UrunAyarlar.this, AdminPaneli.class));
            }
        });
    }
}