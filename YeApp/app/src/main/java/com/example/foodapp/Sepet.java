package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sepet extends AppCompatActivity {
    private ArrayList<Urun> uruns;
    private RecyclerView recyclerView;
    private SepetUrunRecyclerAdapter sepetUrunRecyclerAdapter;
    BottomNavigationView bottomNavigationView;
    TextView araToplamFiyatText,toplamFiyat;
    ImageView backButton;
    int fiyatTopla;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    int adet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sepet);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        viewSettings();
        fillTheArray();
        adet=sepetUrunRecyclerAdapter.urunSayisi;
        sepetUrunRecyclerAdapter.notifyDataSetChanged();
        Button button=findViewById(R.id.odemeyapbtn);
        backButton=findViewById(R.id.imageView2);
        araToplamFiyatText=findViewById(R.id.aratoplam_fiyat);
        toplamFiyat=findViewById(R.id.toplam_fiyat);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sepet.this,Anasayfa.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sepet.this,Odeme.class);
                intent.putExtra("fiyat",fiyatTopla);
                startActivity(intent);
            }
        });

        bottomNavigationView=findViewById(R.id.bottom12);
        bottomNavigationView.setSelectedItemId(R.id.sepet);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.anasayfa:
                        startActivity(new Intent(Sepet.this,Anasayfa.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profil:
                        startActivity(new Intent(Sepet.this,Profil.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sepet:
                        return true;
                }
                return false;
            }
        });
    }

    public void fillTheArray() {

       firebaseFirestore.collection("SepettekiUrunler")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Sepet")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        uruns.clear();
                        fiyatTopla=0;
                        if (e !=null){
                            Toast.makeText(Sepet.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                        if (queryDocumentSnapshots!=null){
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                HashMap<String,Object> data;
                                data = (HashMap<String, Object>) snapshot.getData();
                                ekle(data);
                                sepetUrunRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

    }

    private  void ekle(HashMap<String,Object> data){
        String resimId=(String)data.get("resim");
        String urunAdi =(String) data.get("urunAd");
        int adet=Integer.parseInt(data.get("adet").toString());
        String urunFiyati=(String)data.get("urunFiyat");
        int fiyat=Integer.parseInt(urunFiyati);
        uruns.add(new Urun(resimId,urunAdi,adet,(double)fiyat));
        fiyatTopla += (fiyat*adet);
        araToplamFiyatText.setText(String.valueOf(fiyatTopla));
        toplamFiyat.setText(String.valueOf(fiyatTopla+2));

    }

    private void viewSettings() {
        recyclerView = findViewById(R.id.recyclerview);
        uruns = new ArrayList<>();
        sepetUrunRecyclerAdapter = new SepetUrunRecyclerAdapter(uruns);
        recyclerView.setAdapter(sepetUrunRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}