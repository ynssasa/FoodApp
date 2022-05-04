package com.example.foodapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AnasayfaUrunRecyclerAdapter extends RecyclerView.Adapter<AnasayfaUrunRecyclerAdapter.MyViewHolder> {
    private ArrayList<Urun> uruns;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView urunAdi,urunAdet,urunFiyat;
        ImageView urunResim;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            urunResim = itemView.findViewById(R.id.anasayfa_urun_resim);
            urunAdi = itemView.findViewById(R.id.anasayfa_urun_adi);
            urunAdet = itemView.findViewById(R.id.anasayfa_urun_adet);
            urunFiyat = itemView.findViewById(R.id.anasayfa_urun_fiyat);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);

        }
    }
    public AnasayfaUrunRecyclerAdapter(ArrayList<Urun> uruns) {
        this.uruns = uruns;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urun_single_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Urun currentItem = uruns.get(position);
        holder.urunAdi.setText(currentItem.getUrunAdi());
        holder.urunAdet.setText(String.valueOf(currentItem.getUrunAdet()));
        holder.urunFiyat.setText(String.valueOf(currentItem.getUrunFiyat())+"₺");
        String urlImg=uruns.get(position).getUrunResim();
        Picasso.get().load(urlImg).into(holder.urunResim);

    }

    @Override
    public int getItemCount() {
        return uruns.size();
    }


}
