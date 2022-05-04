package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class SiparisDetay extends AppCompatActivity {
    private ArrayList<Urun> uruns;
    private RecyclerView recyclerView;
    private SiparisUrunRecyclerAdapter siparisUrunRecyclerAdapter;
    FirebaseFirestore firebaseFirestore;
    Bundle bundle;
    FirebaseAuth firebaseAuth;
    BottomNavigationView bottomNavigationView;
    TextView textViewId,araToplam,toplam,adresSiparis,textViewTamam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_detay);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        textViewId=findViewById(R.id.textView7);
        araToplam=findViewById(R.id.aratoplam_fiyat);
        toplam=findViewById(R.id.toplam_fiyat);
        adresSiparis=findViewById(R.id.adresSiparis);
        textViewTamam=findViewById(R.id.textView4);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        bundle = getIntent().getExtras();

        if(bundle.getString("id")!= null)
        {
            textViewId.setText(bundle.getString("id"));
            araToplam.setText(bundle.getString("fiyat"));
        }

        viewSettings();
        if (firebaseAuth.getCurrentUser().getUid().equals("Zd4534HQhccnJc0AHXHC6bhByLM2"))
        {
            textViewTamam.setVisibility(View.VISIBLE);
            adresGetirAdmin();
        }else {
            textViewTamam.setVisibility(View.INVISIBLE);
            adresGetir();
            fillTheArray(textViewId.getText().toString());
        }

        siparisUrunRecyclerAdapter.notifyDataSetChanged();

        textViewTamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SiparisDetay.this, "Sipariş tamamlandı", Toast.LENGTH_SHORT).show();
                adminSiparisSil();
                startActivity(new Intent(SiparisDetay.this,Siparisler.class));
                overridePendingTransition(0,0);
            }
        });

        double fiy=Double.parseDouble(araToplam.getText().toString())+2;
        toplam.setText((String.valueOf(fiy)));
        bottomNavigationView=findViewById(R.id.bottom12);
        bottomNavigationView.setSelectedItemId(R.id.profil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.anasayfa:
                        startActivity(new Intent(SiparisDetay.this,Anasayfa.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profil:
                        startActivity(new Intent(SiparisDetay.this,Profil.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sepet:
                        startActivity(new Intent(SiparisDetay.this,Sepet.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }


    private void fillTheArray(String id) {
        firebaseFirestore.collection("SiparisDetay")
                .document(id)
                .collection("Urunler")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()) {
                                HashMap<String,Object> data;
                                data=(HashMap<String, Object>) document.getData();
                                ekle(data);
                                siparisUrunRecyclerAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });
    }

    private  void ekle(HashMap<String,Object> data){
        String resimId=(String)data.get("resim").toString();
        String urunAdi =(String) data.get("urunAd");
        int adet=Integer.parseInt(data.get("adet").toString());
        String urunFiyati=(String)data.get("urunFiyat");
        int fiyat=Integer.parseInt(urunFiyati);
        uruns.add(new Urun(resimId,urunAdi,adet,(double)fiyat));

    }

    private void viewSettings() {
        recyclerView = findViewById(R.id.recyclerview);
        uruns = new ArrayList<>();
        siparisUrunRecyclerAdapter = new SiparisUrunRecyclerAdapter(uruns);
        recyclerView.setAdapter(siparisUrunRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void  adresGetir(){
        firebaseFirestore.collection("Users")
                .whereEqualTo("id",firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()) {
                                HashMap<String,Object> user;
                                user=(HashMap<String, Object>) document.getData();
                                String adress=user.get("adres").toString();
                                adresSiparis.setText(adress);
                            }
                        }
                    }
                });
    }

    public void  adresGetirAdmin(){
        firebaseFirestore.collection("AdminGorunecekSiparisler")
                .document("Zd4534HQhccnJc0AHXHC6bhByLM2")
                .collection("Siparis")
                .document(textViewId.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        HashMap<String,Object> user;
                        user=(HashMap<String, Object>) documentSnapshot.getData();
                        String adress=user.get("adres").toString();
                        adresSiparis.setText(adress);
                        fillTheArray(user.get("siparisId2").toString());
                    }
                });

    }

    public  void adminSiparisSil(){
        firebaseFirestore.collection("AdminGorunecekSiparisler")
                .document("Zd4534HQhccnJc0AHXHC6bhByLM2")
                .collection("Siparis")
                .document(textViewId.getText().toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }


}