package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UrunListAdapter extends ArrayAdapter {

    List<String> urunAdi;
    List<String> urunResim;
    List<String> urunFiyat;
    Context context;
    int adet=0;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    public UrunListAdapter(@NonNull Context context, List<String> urunAdi, List<String> urunResim, List<String> urunFiyat) {
        super(context, R.layout.urun_single_item,urunAdi);
        this.urunAdi = urunAdi;
        this.urunResim = urunResim;
        this.urunFiyat=urunFiyat;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.urun_single_item,parent,false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        ImageView imageView = view.findViewById(R.id.anasayfa_urun_resim);
        TextView textView = view.findViewById(R.id.anasayfa_urun_adi);
        TextView textAdet=view.findViewById(R.id.anasayfa_urun_adet);
        ImageView imageViewArttir=view.findViewById(R.id.anasayfa_urun_arttir);
        ImageView imageViewAzalt=view.findViewById(R.id.anasayfa_urun_azalt);
        TextView urunFiyati=view.findViewById(R.id.anasayfa_urun_fiyat);
        Button sepetEkleButton=view.findViewById(R.id.sepete_ekle_btn);
        String resimId=urunResim.get(position);
        adet=Integer.parseInt(textAdet.getText().toString());
        urunFiyati.setText(urunFiyat.get(position));

        imageViewArttir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adet=Integer.parseInt(textAdet.getText().toString());
                adet++;
                textAdet.setText(""+adet);
            }
        });

        imageViewAzalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adet=Integer.parseInt(textAdet.getText().toString());
                if (adet!=0){
                    adet--;
                    textAdet.setText(""+adet);
                }
            }
        });

        sepetEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat sekil = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate=sekil.format(calendar.getTime());
                String userId=firebaseAuth.getCurrentUser().getUid();
                adet=Integer.parseInt(textAdet.getText().toString());
                final HashMap<String,Object> urunHash=new HashMap<>();
                urunHash.put("resim",resimId);
                urunHash.put("userId",userId);
                urunHash.put("date",currentDate);
                urunHash.put("urunAd",textView.getText().toString());
                urunHash.put("urunFiyat",urunFiyati.getText().toString());
                urunHash.put("adet",adet);

                firebaseFirestore.collection("SepettekiUrunler").document(userId)
                        .collection("Sepet").add(urunHash).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });
            
            }
        });
        urunFiyati.setText(urunFiyat.get(position));
        textView.setText(urunAdi.get(position));
        String urlImg =urunResim.get(position);
        Picasso.get().load(urlImg).into(imageView);
        return view;
    }
}
