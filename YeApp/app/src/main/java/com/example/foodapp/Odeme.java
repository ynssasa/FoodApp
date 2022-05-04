package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Odeme extends AppCompatActivity {
    boolean kontrolOdemeSekil=true;
    // odemeKontrol
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    boolean islemBool=false;
    String ownerAdress = "TBA6CypYJizwA9XdC7Ubgc5F1bxrQ7SqPt"; //musteri adresi gonderen
    String toAdress = "T9zzBDkYdrckB7BVHpHnc18brUBFof4PGy"; // dukkan adresi
    double urunTutari = 163510320 / 1000000; //true
    //double urunTutari = 182510320 / 1000000; //false
    // odeme
    String odemeSekli;
    static String ID,USER_ID;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText editAdres;
    int fiyat_toplam;
    TextView toplam_fiyat,aratoplam_fiyat,trxcuzdanadres;
    ImageView kapidaOdeme,coinOdeme,cuzdanAdresCopy,backButton;
    ClipboardManager myClipboard;
    ClipData myClip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odeme);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            fiyat_toplam = bundle.getInt("fiyat");
        }
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Button buttonShow = findViewById(R.id.siparisgonderbtn);
        kapidaOdeme=findViewById(R.id.imageView3);
        coinOdeme=findViewById(R.id.imageView4);
        editAdres=findViewById(R.id.siparisadresi);
        backButton=findViewById(R.id.imageView2);
        trxcuzdanadres=findViewById(R.id.trxcuzdanadres);
        cuzdanAdresCopy=findViewById(R.id.imageView5);
        toplam_fiyat=findViewById(R.id.toplam_fiyat);
        aratoplam_fiyat=findViewById(R.id.aratoplam_fiyat);
        aratoplam_fiyat.setText(String.valueOf(fiyat_toplam));
        toplam_fiyat.setText(String.valueOf(fiyat_toplam+2));
        bottomNavigationView=findViewById(R.id.bottom12);
        USER_ID=firebaseAuth.getCurrentUser().getUid();
        adresGetir();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Odeme.this,Sepet.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
        kapidaOdeme.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                kapidaOdeme.setImageResource(R.drawable.ici_dolu);
                coinOdeme.setImageResource(R.drawable.bos);
                kontrolOdemeSekil=true;
            }
        });

        coinOdeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinOdeme.setImageResource(R.drawable.ici_dolu);
                kapidaOdeme.setImageResource(R.drawable.bos);
                kontrolOdemeSekil=false;
            }
        });
        cuzdanAdresCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = trxcuzdanadres.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Text Copied",Toast.LENGTH_SHORT).show();

            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(kontrolOdemeSekil==true){
                    odemeSekli="Kapıda odeme";
                    siparisOlustur();
                    siparisOlusturulduGoruntu();

                }else {
                    odemeSekli="Coin odeme";
                    new fetchData().start();
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.anasayfa:
                        startActivity(new Intent(Odeme.this,Anasayfa.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.profil:
                        startActivity(new Intent(Odeme.this,Profil.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sepet:
                        startActivity(new Intent(Odeme.this,Sepet.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
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
                                ownerAdress=user.get("cuzdanadress").toString();
                                editAdres.setText(adress);
                            }
                        }
                    }
                });
    }

    class fetchData extends Thread{
        String data="";
        @Override
        public void run(){

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(Odeme.this);
                    progressDialog.setMessage("Sorgulanıyor..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL url = new URL("https://apilist.tronscan.org/api/transaction?sort=-timestamp&count=true&limit=20&start=0&address=T9zzBDkYdrckB7BVHpHnc18brUBFof4PGy");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }
                if (!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray dataList = jsonObject.getJSONArray("data");
                    for (int i =0; i< dataList.length();i++){

                        JSONObject contractData = dataList.getJSONObject(i).getJSONObject("contractData");
                        JSONObject cost = dataList.getJSONObject(i).getJSONObject("cost");
                        int fee = cost.getInt("fee");
                        //int amount = contractData.getInt("amount") + fee;
                        double amount = contractData.getInt("amount") / 1000000;

                        String gonderen = (String) contractData.get("owner_address");
                        String alici = (String) contractData.get("to_address");

                        if (gonderen.equals(ownerAdress) && alici.equals(toAdress) && amount == urunTutari){
                            islemBool = true;
                            String text = "Gonderen : " + gonderen + " -> " + alici + " Amount : " + amount + " Fee : " + fee + " Islem Bool : " + islemBool;
                        }
                        else{
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if (islemBool){
                        siparisOlustur();
                        siparisOlusturulduGoruntu();
                    }
                    else{
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                Odeme.this,R.style.BottomSheetDialogTheme
                        );
                        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                                .inflate(
                                        R.layout.layout_basarisiz_bottom_sheet,
                                        (LinearLayout)findViewById(R.id.bottomSheetContainer)
                                );
                        bottomSheetView.findViewById(R.id.anasayfayadon).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bottomSheetDialog.dismiss();
                            }
                        });
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();
                    }
                }
            });
        }
    }

    private void siparisOlusturulduGoruntu(){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                Odeme.this,R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_basarili_bottom_sheet,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer)
                );
        bottomSheetView.findViewById(R.id.anasayfayadon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void sepetVerileriniCek(String id){
        firebaseFirestore.collection("SepettekiUrunler")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Sepet")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document:task.getResult()) {
                        HashMap<String,Object> data;
                        data=(HashMap<String, Object>) document.getData();
                        urunDocumentIdDeleteBul(data.get("urunAd").toString());
                        siparisVeriTabaniEkle(id,data);
                    }
                }
            }
        });
    }

    private void siparisVeriTabaniEkle(String siparisId,HashMap<String,Object> data){
        firebaseFirestore.collection("SiparisDetay")
                .document(siparisId)
                .collection("Urunler")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });
    }

    public  void urunDocumentIdDeleteBul(String isim){
        firebaseFirestore.collection("SepettekiUrunler").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Sepet")
                .whereEqualTo("urunAd",isim)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot query:queryDocumentSnapshots) {
                    urunDelete(query.getId());
                }
            }
        });

    }

    public  void  urunDelete(String uId){
        firebaseFirestore.collection("SepettekiUrunler")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Sepet")
                .document(uId)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    public void  siparisOlustur(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sekil = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate=sekil.format(calendar.getTime());
        HashMap<String,Object> hashSiparis=new HashMap<>();
        hashSiparis.put("date",currentDate);
        hashSiparis.put("toplam",fiyat_toplam);
        hashSiparis.put("odemeSekli",odemeSekli);
        hashSiparis.put("adres",editAdres.getText().toString());
        firebaseFirestore.collection("Siparisler")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Siparis")
                .add(hashSiparis)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        siparisOlusturGuncelle(documentReference.getId());
                        ID=documentReference.getId();
                        sepetVerileriniCek(documentReference.getId());
                    }
                });
        adminSiparisiGormeOlustur(hashSiparis);
    }

    public void adminSiparisiGormeOlustur(HashMap<String,Object> hashSiparis){
        firebaseFirestore.collection("AdminGorunecekSiparisler")
                .document("Zd4534HQhccnJc0AHXHC6bhByLM2")
                .collection("Siparis")
                .add(hashSiparis)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        adminSiparisGormeGuncelle(documentReference.getId());
                    }
                });
    }

    public  void  adminSiparisGormeGuncelle(String id){
        HashMap<String,Object> hashSiparis=new HashMap<>();
        hashSiparis.put("siparisId",id);
        hashSiparis.put("userId",USER_ID);
        hashSiparis.put("siparisId2",ID);
        firebaseFirestore.collection("AdminGorunecekSiparisler")
                .document("Zd4534HQhccnJc0AHXHC6bhByLM2")
                .collection("Siparis")
                .document(id)
                .update(hashSiparis)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    public void siparisOlusturGuncelle(String id){
        HashMap<String,Object> hashSiparis=new HashMap<>();
        hashSiparis.put("siparisId",id);
        firebaseFirestore.collection("Siparisler")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Siparis")
                .document(id)
                .update(hashSiparis)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }


}