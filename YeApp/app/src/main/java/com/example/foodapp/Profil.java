package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Profil extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView siparislerimGit,cikisYapTxt,urunAyarlarTxt;
    TextView isim,adres,telNo,email,profilTextIsim,sifre,sifre2,cuzdanAdress;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    Button updateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        firebaseFirestore=FirebaseFirestore.getInstance();
        updateButton=findViewById(R.id.updateButton);
        profilTextIsim=findViewById(R.id.textView2);
        isim=findViewById(R.id.name);
        adres=findViewById(R.id.adress);
        telNo=findViewById(R.id.phoneNo);
        email=findViewById(R.id.emailAdress);
        sifre=findViewById(R.id.password);
        sifre2=findViewById(R.id.confirmPassword);
        cuzdanAdress=findViewById(R.id.cuzdanAdress);
        siparislerimGit = findViewById(R.id.siparislerimGit);
        cikisYapTxt = findViewById(R.id.cikisYapTxt);
        urunAyarlarTxt = findViewById(R.id.urunAyarlarTxt);
        bottomNavigationView=findViewById(R.id.bottom12);
        bottomNavigationView.setSelectedItemId(R.id.profil);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        String id=firebaseAuth.getCurrentUser().getUid();
        if (!id.equals("Zd4534HQhccnJc0AHXHC6bhByLM2")){
            urunAyarlarTxt.setVisibility(View.INVISIBLE);
        }
        firebaseFirestore.collection("Users")
                .whereEqualTo("id",id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()){
                   for (QueryDocumentSnapshot document:task.getResult()) {
                            HashMap<String,Object> user;
                            user=(HashMap<String, Object>) document.getData();
                           String name=user.get("isim").toString();
                           String adress=user.get("adres").toString();
                           String cuzdanAdresi=user.get("cuzdanadress").toString();
                           String mail=user.get("email").toString();
                           String telefon=user.get("telefon").toString();
                           String pass=user.get("password").toString();
                           adres.setText(adress);
                           cuzdanAdress.setText(cuzdanAdresi);
                           profilTextIsim.setText("Merhaba "+name);
                           isim.setText(name);
                           telNo.setText(telefon);
                           email.setText(mail);
                           sifre.setText(pass);
                           sifre2.setText(pass);
                   }
               }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,adress,mail,telefon,sifree,sifree2,cuzdanAdres;
                name=isim.getText().toString();
                adress=adres.getText().toString();
                telefon=telNo.getText().toString();
                mail=email.getText().toString();
                sifree=sifre.getText().toString();
                sifree2=sifre2.getText().toString();
                cuzdanAdres=cuzdanAdress.getText().toString();
                if (!sifree.equals(null)&&!sifree.equals("")&&sifree.equals(sifree2)){
                    firebaseUser.updatePassword(sifree).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }else {
                    sifree="";
                }
                String userId=firebaseAuth.getCurrentUser().getUid();
                HashMap<String,Object> userMap=new HashMap<>();
                userMap.put("id",userId);
                userMap.put("isim",name);
                userMap.put("adres",adress);
                userMap.put("telefon",telefon);
                userMap.put("email",mail);
                userMap.put("password",sifree);
                userMap.put("cuzdanadress",cuzdanAdres);
                firebaseFirestore.collection("Users").document(userId).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Profil.this,"Profil uptade",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Profil.this, Profil.class));
                        finish();
                    }
                });
            }
        });

        urunAyarlarTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profil.this, UrunAyarlar.class));
            }
        });
        cikisYapTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(Profil.this, MainActivity.class));
                finish();
            }
        });
        siparislerimGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profil.this,Siparisler.class));
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.anasayfa:
                        startActivity(new Intent(Profil.this,Anasayfa.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.profil:
                        return true;
                    case R.id.sepet:
                        startActivity(new Intent(Profil.this,Sepet.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

}