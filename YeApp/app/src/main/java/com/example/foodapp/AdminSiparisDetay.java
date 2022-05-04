package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class AdminSiparisDetay extends AppCompatActivity {
    private ArrayList<Urun> uruns;
    private RecyclerView recyclerView;
    private SiparisUrunRecyclerAdapter siparisUrunRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_siparis_detay);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        viewSettings();
        fillTheArray();
        siparisUrunRecyclerAdapter.notifyDataSetChanged();
    }

    private void fillTheArray() {
        uruns.add(new Urun("","Burger",1,5));
        uruns.add(new Urun("","Pizza",2,15));
        uruns.add(new Urun("","Burger",1,5));
        uruns.add(new Urun("","Pizza",2,15));
        uruns.add(new Urun("","Burger",1,5));
        uruns.add(new Urun("","Pizza",2,15));
    }

    private void viewSettings() {
        recyclerView = findViewById(R.id.recyclerview);
        uruns = new ArrayList<>();
        siparisUrunRecyclerAdapter = new SiparisUrunRecyclerAdapter(uruns);
        recyclerView.setAdapter(siparisUrunRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}