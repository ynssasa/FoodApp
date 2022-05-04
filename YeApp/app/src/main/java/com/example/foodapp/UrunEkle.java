package com.example.foodapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UrunEkle extends AppCompatActivity {
    ImageView backButton,resimEkleImg;
    FirebaseStorage storage;
    Uri imageUri;
    int docSize;
    TextView urunAdi,urunFiyati;
    Spinner spinnerKategori;
    String secilenKategori,secilenUrunId;
    ArrayList<CharSequence> kategoriList;
    ArrayAdapter<CharSequence> adapterKategori;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ekle);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        backButton = findViewById(R.id.backButton);
        resimEkleImg = findViewById(R.id.resimEkleImg);
        urunAdi = findViewById(R.id.urunAdi);
        urunFiyati = findViewById(R.id.urunFiyati);
        storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        kategoriList = new ArrayList<>();
        kategoriList.add(0,"Kategori Seç");
        spinnerInit();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UrunEkle.this, UrunAyarlar.class));
            }
        });
    }

    public void selectImage(View view){

        mGetContent.launch("image/*");

       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        }*/

    }
    public void upload(View view){

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Resim Yükleniyor...");
        dialog.show();

        if (imageUri != null){
            final String imageName = "urunler/"+UUID.randomUUID().toString()+".jpg";
            StorageReference reference = storage.getReference().child(imageName);
            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // yüklenen resim url
                                String downloadUrl = uri.toString();
                                getDataFromUrunlerFirestore();
                                HashMap<String, Object> urunData = new HashMap<>();
                                urunData.put("id", FieldValue.increment(docSize+1));
                                urunData.put("urunAdi", urunAdi.getText().toString());
                                urunData.put("urunFiyati", urunFiyati.getText().toString());
                                urunData.put("kategoriAdi", secilenKategori);
                                urunData.put("downloadurl", downloadUrl);

                                firebaseFirestore.collection("Urunler").add(urunData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        secilenUrunId = documentReference.getId();
                                        updateUrun();
                                        Toast.makeText(UrunEkle.this, "Data eklendi", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UrunEkle.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                        dialog.dismiss();
                        Toast.makeText(UrunEkle.this, "Resim Yüklendi.", Toast.LENGTH_SHORT).show();
                    }else{
                        dialog.dismiss();
                        Toast.makeText(UrunEkle.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result !=null){
                        resimEkleImg.setImageURI(result);
                        imageUri = result;
                    }
                }
            });
    private void spinnerInit(){
        getDataFromKategorilerFirestore();
        spinnerKategori = findViewById(R.id.urunKategori);
        adapterKategori = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,kategoriList);
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(adapterKategori);
        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //textSecilen.setText(parent.getItemAtPosition(position).toString());
                if (parent.getItemAtPosition(position).equals("Kategori Seç")){

                }
                else{
                    Toast.makeText(UrunEkle.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    secilenKategori = parent.getItemAtPosition(position).toString();
                    
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //spinnerKategori.setSelection(0);
            }
        });
    }

    public void getDataFromKategorilerFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Kategoriler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e !=null){
                    Toast.makeText(UrunEkle.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }

                if (queryDocumentSnapshots!=null){

                    //docSize = 0;
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        //docSize=docSize+1;
                        Map<String,Object> data = snapshot.getData();
                        String kategoriAdi = (String) data.get("kategoriAdi");
                        kategoriList.add(kategoriAdi);

                    }
                }
            }
        });
    }

    public void getDataFromUrunlerFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Urunler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e !=null){
                    Toast.makeText(UrunEkle.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }

                if (queryDocumentSnapshots!=null){

                    docSize = 0;
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        docSize=docSize+1;
                        Map<String,Object> data = snapshot.getData();
                        //String kategoriAdi = (String) data.get("kategoriAdi");
                        //kategoriList.add(kategoriAdi);

                    }
                }
            }
        });
    }


    public void updateUrun(){


                HashMap<String,Object> urunMap=new HashMap<>();
                urunMap.put("id",secilenUrunId);



                firebaseFirestore.collection("Urunler").document(secilenUrunId).update(urunMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(UrunGuncelle.this,"UrunGuncelle uptade",Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(UrunGuncelle.this, UrunGuncelle.class));
                        //finish();
                    }
                });
    }




}