package com.example.foodapp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class SepetUrunRecyclerAdapter extends RecyclerView.Adapter<SepetUrunRecyclerAdapter.MyViewHolder> {
    private ArrayList<Urun> uruns;
    int adet,araToplam;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    public SepetUrunRecyclerAdapter(ArrayList<Urun> uruns) {
        this.uruns = uruns;
    }
    public int urunSayisi;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sepet_single_item,parent,false);
        urunSayisi=uruns.size();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        holder.urunAdi.setText(uruns.get(position).getUrunAdi());
        holder.urunAdet.setText(String.valueOf(uruns.get(position).getUrunAdet()));
        holder.urunFiyat.setText(String.valueOf(uruns.get(position).getUrunFiyat()));
        String urlImg=uruns.get(position).getUrunResim();
        Picasso.get().load(urlImg).into(holder.urunResim);
        adet=Integer.parseInt(String.valueOf(uruns.get(position).getUrunAdet()));
        holder.adetAzalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adet!=0){
                    adet=Integer.parseInt(holder.urunAdet.getText().toString());
                    adet--;
                    urunDocumentIdUpdateBul(holder.urunAdi.getText().toString(),adet);
                    holder.urunAdet.setText(""+adet);
                }
            }
        });

        holder.adetArttir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adet=Integer.parseInt(holder.urunAdet.getText().toString());
                adet++;
                urunDocumentIdUpdateBul(holder.urunAdi.getText().toString(),adet);
                holder.urunAdet.setText(""+adet);

            }
        });

        holder.urunSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urunDocumentIdDeleteBul(holder.urunAdi.getText().toString());
                urunSayisi--;
            }
        });

    }

    @Override
    public int getItemCount() {
        return uruns.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView urunAdi,urunAdet,urunFiyat;
        ImageView urunResim,adetAzalt,adetArttir,urunSil;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            urunResim = itemView.findViewById(R.id.sepet_urun_resim);
            urunAdi = itemView.findViewById(R.id.sepet_urun_adi);
            urunAdet = itemView.findViewById(R.id.sepet_urun_adet);
            urunFiyat = itemView.findViewById(R.id.sepet_urun_fiyat);
            adetAzalt=itemView.findViewById(R.id.sepet_urun_azalt);
            adetArttir=itemView.findViewById(R.id.sepet_urun_arttir);
            urunSil=itemView.findViewById(R.id.sepet_urun_sil);
        }
    }

    public  void urunDocumentIdUpdateBul(String isim,int adet){
        firebaseFirestore.collection("SepettekiUrunler").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Sepet")
                .whereEqualTo("urunAd",isim)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot query:queryDocumentSnapshots) {
                    urunUpdate(query.getId(),adet);
                }
            }
        });

    }

    public  void urunUpdate(String id,int adet){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("adet",adet);
        firebaseFirestore.collection("SepettekiUrunler")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Sepet")
                .document(id)
                .update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

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
}
