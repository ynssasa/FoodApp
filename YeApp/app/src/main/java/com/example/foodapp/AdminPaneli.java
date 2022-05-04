package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminPaneli extends AppCompatActivity {
    private ArrayList<SiparisUrun> siparisUruns;
    private RecyclerView recyclerView;
    private AdminSiparislerRecyclerAdapter adminSiparislerRecyclerAdapter;
    TextView urunAyarlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_paneli);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        viewSettings();
        fillTheArray();
        adminSiparislerRecyclerAdapter.notifyDataSetChanged();
        urunAyarlar = findViewById(R.id.textView11);
        urunAyarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPaneli.this, UrunAyarlar.class));
            }
        });

    }
    private void fillTheArray() {
        siparisUruns.add(new SiparisUrun("","Sipariş Numarası: 1",1,"18.01.2022"));
        siparisUruns.add(new SiparisUrun("","Sipariş Numarası: 1",2,"18.01.2022"));
        siparisUruns.add(new SiparisUrun("","Sipariş Numarası: 1",1,"18.01.2022"));
        siparisUruns.add(new SiparisUrun("","Sipariş Numarası: 1",2,"18.01.2022"));
        siparisUruns.add(new SiparisUrun("","Sipariş Numarası: 1",1,"18.01.2022"));
        siparisUruns.add(new SiparisUrun("","Sipariş Numarası: 1",2,"18.01.2022"));
    }

    private void viewSettings() {
        recyclerView = findViewById(R.id.recyclerview);
        siparisUruns = new ArrayList<>();
        adminSiparislerRecyclerAdapter = new AdminSiparislerRecyclerAdapter(AdminPaneli.this,siparisUruns);

        recyclerView.setAdapter(adminSiparislerRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}