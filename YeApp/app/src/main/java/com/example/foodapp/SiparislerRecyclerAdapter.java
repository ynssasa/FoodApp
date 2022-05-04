package com.example.foodapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SiparislerRecyclerAdapter extends RecyclerView.Adapter<SiparislerRecyclerAdapter.MyViewHolder> {
    private ArrayList<SiparisUrun> siparisUruns;
    Context c;
    public SiparislerRecyclerAdapter(Context c, ArrayList<SiparisUrun> siparisUruns) {
        this.siparisUruns = siparisUruns;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.siparis_single_urun,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.siparisId.setText(siparisUruns.get(position).getSiparisId());
        holder.siparisTarih.setText(siparisUruns.get(position).getSiparisTarih());
        holder.siparisFiyat.setText(String.valueOf(siparisUruns.get(position).getSiparisFiyat()));
        String urlImg = "https://firebasestorage.googleapis.com/v0/b/yeapp-3540e.appspot.com/o/images%2Ffood_png.png?alt=media&token=1b115a3e-994e-4419-8358-5dfd46b35a48";
        Picasso.get().load(urlImg).into(holder.siparisResim);
        holder.siparisDetay.setText(siparisUruns.get(position).getSiparisDetay());

        holder.siparisDetay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,SiparisDetay.class);
                intent.putExtra("date",holder.siparisTarih.getText().toString());
                intent.putExtra("id",holder.siparisId.getText().toString());
                intent.putExtra("fiyat",holder.siparisFiyat.getText().toString());
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return siparisUruns.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView siparisId,siparisFiyat,siparisTarih,siparisDetay;
        ImageView siparisResim;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            siparisResim = itemView.findViewById(R.id.siparis_urun_resim);
            siparisId = itemView.findViewById(R.id.siparisId);
            siparisTarih = itemView.findViewById(R.id.siparis_tarih);
            siparisFiyat = itemView.findViewById(R.id.siparis_urun_fiyat);
            siparisDetay = itemView.findViewById(R.id.siparisDetay);

        }
    }
}
