package com.example.foodapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KategoriEkle extends AppCompatActivity {
    ImageView backButton,resimEkleImg,resimEkleImg2;
    TextView kategoriAdi,textView;
    String downloadUrl2;
    int docSize;
    FirebaseStorage storage;
    FirebaseFirestore firebaseFirestore;
    Uri imageUri,imageUri2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_ekle);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        backButton = findViewById(R.id.backButton);
        resimEkleImg = findViewById(R.id.resimEkleImg);
        resimEkleImg2 = findViewById(R.id.resimEkleImg2);
        kategoriAdi = findViewById(R.id.kategoriAdi);
        textView = findViewById(R.id.textView);
        storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getDataFromFirestore();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KategoriEkle.this, UrunAyarlar.class));
            }
        });
    }

    public void getDataFromFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Kategoriler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e !=null){
                    Toast.makeText(KategoriEkle.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }

                if (queryDocumentSnapshots!=null){

                    docSize = 0;
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        docSize=docSize+1;
                        Map<String,Object> data = snapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        System.out.println(downloadUrl);
                    }
                }
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

    public void selectImage2(View view){

        mGetContent2.launch("image/*");

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

        if (imageUri2 != null){
            final String imageName2 = "kategoriResim/"+ UUID.randomUUID().toString()+".jpg";
            StorageReference reference2 = storage.getReference().child(imageName2);
            reference2.putFile(imageUri2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        StorageReference newReference2 = FirebaseStorage.getInstance().getReference(imageName2);
                        newReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // yüklenen resim url
                                downloadUrl2 = uri.toString();
                            }
                        });
                        dialog.dismiss();
                        Toast.makeText(KategoriEkle.this, "Resim Yüklendi.", Toast.LENGTH_SHORT).show();
                    }else{
                        dialog.dismiss();
                        Toast.makeText(KategoriEkle.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (imageUri != null){
            final String imageName = "kategoriResim/"+ UUID.randomUUID().toString()+".jpg";
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
                                downloadUrl2 = downloadUrl;
                                HashMap<String, Object> kategoriData = new HashMap<>();
                                kategoriData.put("id", FieldValue.increment(docSize+1));
                                kategoriData.put("kategoriAdi", kategoriAdi.getText().toString());
                                kategoriData.put("downloadurl", downloadUrl);
                                kategoriData.put("downloadurl2", downloadUrl2);

                                firebaseFirestore.collection("Kategoriler").add(kategoriData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(KategoriEkle.this, "Data eklendi", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(KategoriEkle.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        dialog.dismiss();
                        Toast.makeText(KategoriEkle.this, "Resim Yüklendi.", Toast.LENGTH_SHORT).show();
                    }else{
                        dialog.dismiss();
                        Toast.makeText(KategoriEkle.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    ActivityResultLauncher<String> mGetContent2 = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result !=null){
                        resimEkleImg2.setImageURI(result);
                        imageUri2 = result;
                    }
                }
            });
}