package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UrunSil extends AppCompatActivity {
    private Spinner spinnerUrun;
    private TextView textSecilen;
    String secilenUrunId;
    Button deleteButton;
    private ArrayList<CharSequence> urunList;
    private ArrayAdapter<CharSequence> adapterUrun;
    FirebaseFirestore firebaseFirestore;
    private void spinnerInit(){
        getDataFromUrunlerFirestore();
        spinnerUrun = findViewById(R.id.urunListesi);
        textSecilen = findViewById(R.id.textSecilen);

        urunList = new ArrayList<>();
        urunList.add("Urun Seç");


        adapterUrun = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,urunList);
        adapterUrun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUrun.setAdapter(adapterUrun);

        spinnerUrun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Urun Seç")){

                }
                else{
                    Toast.makeText(UrunSil.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    textSecilen.setText(parent.getItemAtPosition(position).toString());

                }
                //textSecilen.setText(parent.getItemAtPosition(position).toString());
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
        setContentView(R.layout.activity_urun_sil);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        firebaseFirestore = FirebaseFirestore.getInstance();
        deleteButton = findViewById(R.id.deleteButton);
        spinnerInit();
    }

    public void getDataFromUrunlerFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Urunler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e !=null){
                    Toast.makeText(UrunSil.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
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
                                secilenUrunId = document.getId();
                                //Toast.makeText(UrunSil.this, secilenUrunId, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void urunSil(View view){
        firebaseFirestore.collection("Urunler")
                .whereEqualTo("urunAdi",textSecilen.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()) {
                                firebaseFirestore.collection("Urunler").document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(UrunSil.this, "Silindi.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UrunSil.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }
                });

    }

}