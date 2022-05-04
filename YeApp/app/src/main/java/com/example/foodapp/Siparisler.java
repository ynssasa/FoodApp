package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Siparisler extends AppCompatActivity {
    private ArrayList<SiparisUrun> siparisUruns;
    private RecyclerView recyclerView;
    private SiparislerRecyclerAdapter siparislerRecyclerAdapter;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparisler);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        viewSettings();
        if (firebaseAuth.getCurrentUser().getUid().equals("Zd4534HQhccnJc0AHXHC6bhByLM2")){
            fillTheArrayAdmin();
        }else {
            fillTheArray();
        }

        siparislerRecyclerAdapter.notifyDataSetChanged();
        bottomNavigationView=findViewById(R.id.bottom12);
        bottomNavigationView.setSelectedItemId(R.id.profil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.anasayfa:
                        startActivity(new Intent(Siparisler.this,Anasayfa.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profil:
                        startActivity(new Intent(Siparisler.this,Profil.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sepet:
                        startActivity(new Intent(Siparisler.this,Sepet.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }

    private void fillTheArray() {
        firebaseFirestore.collection("Siparisler")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Siparis")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document:task.getResult()) {
                        HashMap<String,Object> data;
                        data=(HashMap<String, Object>) document.getData();
                        ekle(data);
                        siparislerRecyclerAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

    private void fillTheArrayAdmin() {
        firebaseFirestore.collection("AdminGorunecekSiparisler")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Siparis")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document:task.getResult()) {
                        HashMap<String,Object> data;
                        data=(HashMap<String, Object>) document.getData();
                        ekle(data);
                        siparislerRecyclerAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

    private  void ekle(HashMap<String,Object> data){
        String siparisId =(String) data.get("siparisId");
        String tarih =(String) data.get("date");
        int urunFiyati= Integer.parseInt(data.get("toplam").toString());
        siparisUruns.add(new SiparisUrun(siparisId,siparisId,urunFiyati,tarih));

    }

    private void viewSettings() {
        recyclerView = findViewById(R.id.recyclerview);
        siparisUruns = new ArrayList<>();
        siparislerRecyclerAdapter = new SiparislerRecyclerAdapter(Siparisler.this,siparisUruns);

        recyclerView.setAdapter(siparislerRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}