package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UrunGuncelle extends AppCompatActivity {

    private Spinner spinnerUrun;
    private TextView textSecilen,urunAdi,urunFiyati,urunFiyatSecilen;
    private ArrayList<CharSequence> urunList;
    private ArrayAdapter<CharSequence> adapterUrun;
    Button updateButton;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    String secilenUrun,secilenUrunFiyati,secilenUrunId;
    private void spinnerInit(){
        getDataFromUrunlerFirestore();
        spinnerUrun = findViewById(R.id.urunListesi);
        textSecilen = findViewById(R.id.textSecilen);
        urunAdi = findViewById(R.id.urunAdi);
        updateButton = findViewById(R.id.updateButton);
        urunFiyati = findViewById(R.id.urunFiyati);

        adapterUrun = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,urunList);
        adapterUrun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUrun.setAdapter(adapterUrun);

        spinnerUrun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //textSecilen.setText(parent.getItemAtPosition(position).toString());
                if (parent.getItemAtPosition(position).equals("Urun Seç")){

                }
                else{
                    Toast.makeText(UrunGuncelle.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    secilenUrun = parent.getItemAtPosition(position).toString();
                    textSecilen.setText(secilenUrun);
                    secilenUrunBilgileri(secilenUrun);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //textSecilen.setText(parent.getItemAtPosition(0).toString());
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_guncelle);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        urunList = new ArrayList<>();
        urunList.add(0,"Urun Seç");
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        spinnerInit();
        updateUrun();

    }

    public void getDataFromUrunlerFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Urunler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e !=null){
                    Toast.makeText(UrunGuncelle.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }

                if (queryDocumentSnapshots!=null){

                    //docSize = 0;
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        //docSize=docSize+1;
                        Map<String,Object> data = snapshot.getData();
                        //String kategoriAdi = (String) data.get("kategoriAdi");
                        String urunAdi = (String) data.get("urunAdi");

                        urunList.add(urunAdi);
                    }
                }
            }
        });
    }




    public void secilenUrunBilgileri(String isim){
        firebaseFirestore.collection("Urunler")
                .whereEqualTo("urunAdi",isim)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()) {
                                HashMap<String,Object> urunler;
                                urunler=(HashMap<String, Object>) document.getData();
                                String urunIsmi=urunler.get("urunAdi").toString();
                                String urunFiyat=urunler.get("urunFiyati").toString();
                                secilenUrunId = document.getId();
                                urunAdi.setText(urunIsmi);
                                urunFiyati.setText(urunFiyat);

                            }
                        }
                    }
                });
    }

    public void updateUrun(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urunIsim,urunFiyat;
                urunIsim=urunAdi.getText().toString();
                urunFiyat=urunFiyati.getText().toString();

                HashMap<String,Object> urunMap=new HashMap<>();
                urunMap.put("id",secilenUrunId);
                urunMap.put("urunAdi",urunIsim);
                urunMap.put("urunFiyati",urunFiyat);

                firebaseFirestore.collection("Urunler").document(secilenUrunId).delete();
                firebaseFirestore.collection("Urunler").document(secilenUrunId).update(urunMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UrunGuncelle.this,"UrunGuncelle uptade",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UrunGuncelle.this, UrunGuncelle.class));
                        finish();
                    }
                });
            }
        });
    }
}