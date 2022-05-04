package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Anasayfa extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    BottomNavigationView bottomNavigationView;
    private ListView listUrun;
    public UrunListAdapter urunListAdapter;
    List<String> urunAdi;
    List<String> urunFiyat;
    List<String> urunResmi;

    RecyclerView recyclerViewCategory;
    AnasayfaRecyclerAdapter mainAdapter;
    ArrayList<AnasayfaModel> mainModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerViewCategory=findViewById(R.id.recyclerview);
        mainModels=new ArrayList<>();
        mainModelEkle();
        LinearLayoutManager layoutManager=new LinearLayoutManager(Anasayfa.this
                ,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        mainAdapter=new AnasayfaRecyclerAdapter(Anasayfa.this,mainModels,this);
        recyclerViewCategory.setAdapter(mainAdapter);

        listUrun = findViewById(R.id.listUrun);

        urunAdi = new ArrayList<>();
        urunFiyat = new ArrayList<>();
        urunResmi = new ArrayList<>();
        urunBul("Pizza");
        urunListAdapter = new UrunListAdapter(this,urunAdi,urunResmi,urunFiyat);
        listUrun.setAdapter(urunListAdapter);

        bottomNavigationView=findViewById(R.id.bottom12);
        bottomNavigationView.setSelectedItemId(R.id.anasayfa);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.anasayfa:
                        return true;
                    case R.id.profil:
                        startActivity(new Intent(Anasayfa.this,Profil.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sepet:
                        startActivity(new Intent(Anasayfa.this,Sepet.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void mainModelEkle(){
        firebaseFirestore.collection("Kategoriler")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document:task.getResult()) {
                        HashMap<String,Object> data;
                        data=(HashMap<String, Object>) document.getData();
                        ekle(data);
                        mainAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

    private void ekle(HashMap<String,Object> data){
        AnasayfaModel model=new AnasayfaModel((String) data.get("downloadurl"),(String) data.get("downloadurl2"),(String) data.get("kategoriAdi"));
        mainModels.add(model);
    }

    public void setle(String kategoriAdi){
            urunAdi.clear();
            urunResmi.clear();
            urunFiyat.clear();
            urunBul(kategoriAdi);
            urunListAdapter = new UrunListAdapter(this,urunAdi,urunResmi,urunFiyat);
            listUrun.setAdapter(urunListAdapter);

    }

    private  void urunBul(String kategoriAdi){
        firebaseFirestore.collection("Urunler")
                .whereEqualTo("kategoriAdi",kategoriAdi)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()) {
                                HashMap<String,Object> urunHash;
                                urunHash=(HashMap<String, Object>) document.getData();
                                urunEkle(urunHash);
                                urunListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void urunEkle(HashMap<String,Object> urunHash)
    {
        urunAdi.add((String) urunHash.get("urunAdi"));
        urunResmi.add((String) urunHash.get("downloadurl"));
        urunFiyat.add((String) urunHash.get("urunFiyati"));
    }
}