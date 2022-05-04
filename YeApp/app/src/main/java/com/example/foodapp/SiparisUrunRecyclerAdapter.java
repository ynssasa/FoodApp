package com.example.foodapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SiparisUrunRecyclerAdapter extends RecyclerView.Adapter<SiparisUrunRecyclerAdapter.MyViewHolder> {
    private ArrayList<Urun> uruns;

    public SiparisUrunRecyclerAdapter(ArrayList<Urun> uruns) {
        this.uruns = uruns;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.siparis_detay_urun,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.urunAdi.setText(uruns.get(position).getUrunAdi());
        holder.urunAdet.setText(String.valueOf(uruns.get(position).getUrunAdet()));
        holder.urunFiyat.setText(String.valueOf(uruns.get(position).getUrunFiyat())+"₺");
        String urlImg=uruns.get(position).getUrunResim();
        Picasso.get().load(urlImg).into(holder.urunResim);
    }

    @Override
    public int getItemCount() {
        return uruns.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView urunAdi,urunAdet,urunFiyat;
        ImageView urunResim;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            urunResim = itemView.findViewById(R.id.sepet_urun_resim);
            urunAdi = itemView.findViewById(R.id.sepet_urun_adi);
            urunAdet = itemView.findViewById(R.id.sepet_urun_adet);
            urunFiyat = itemView.findViewById(R.id.sepet_urun_fiyat);

        }
    }
}
