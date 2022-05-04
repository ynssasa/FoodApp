package com.example.foodapp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnasayfaRecyclerAdapter extends RecyclerView.Adapter<AnasayfaRecyclerAdapter.ViewHolder> {

    public int pozisyon;
    ArrayList<AnasayfaModel> mainModels;
    Context context;
    Anasayfa mainActivity;


    public AnasayfaRecyclerAdapter(Context context,ArrayList<AnasayfaModel> mainModels,Anasayfa mainActivity){
        this.context=context;
        this.mainModels=mainModels;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String urlImg =mainModels.get(position).langLogoArka;
        Picasso.get().load(urlImg).into(holder.imageView);
        holder.textView.setText(mainModels.get(position).langName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pozisyon = position;
                mainActivity.setle(mainModels.get(position).langName);
                notifyDataSetChanged();
            }
        });

        if (pozisyon!=position){
            urlImg=mainModels.get(position).langLogo;
            Picasso.get().load(urlImg).into(holder.imageView);
        }else {
            urlImg=mainModels.get(position).langLogoArka;
            Picasso.get().load(urlImg).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.text_view);
        }
    }


}