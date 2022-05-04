package com.example.foodapp;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AnasayfaUrun {
    private int urunResim;
    private String urunAdi;
    private double urunFiyati;
    private int urunAdet;
    private int urunAzalt;
    private int urunArttir;
    private Button sepeteEkle;

    public AnasayfaUrun(int urunResim, String urunAdi, double urunFiyati, int urunAdet, int urunAzalt, int urunArttir, Button sepeteEkle) {
        this.urunResim = urunResim;
        this.urunAdi = urunAdi;
        this.urunFiyati = urunFiyati;
        this.urunAdet = urunAdet;
        this.urunAzalt = urunAzalt;
        this.urunArttir = urunArttir;
        this.sepeteEkle = sepeteEkle;
    }

    public int getUrunResim() {
        return urunResim;
    }

    public void setUrunResim(int urunResim) {
        this.urunResim = urunResim;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public double getUrunFiyati() {
        return urunFiyati;
    }

    public void setUrunFiyati(double urunFiyati) {
        this.urunFiyati = urunFiyati;
    }

    public int getUrunAdet() {
        return urunAdet;
    }

    public void setUrunAdet(int urunAdet) {
        this.urunAdet = urunAdet;
    }

    public int getUrunAzalt() {
        return urunAzalt;
    }

    public void setUrunAzalt(int urunAzalt) {
        this.urunAzalt = urunAzalt;
    }

    public int getUrunArttir() {
        return urunArttir;
    }

    public void setUrunArttir(int urunArttir) {
        this.urunArttir = urunArttir;
    }
}
