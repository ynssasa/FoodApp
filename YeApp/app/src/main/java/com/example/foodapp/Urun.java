package com.example.foodapp;

public class Urun {
    private String urunResim;
    private String urunAdi;
    private int urunAdet;
    private double urunFiyat;

    public Urun(String urunResim, String urunAdi, int urunAdet, double urunFiyat) {
        this.urunResim = urunResim;
        this.urunAdi = urunAdi;
        this.urunAdet = urunAdet;
        this.urunFiyat = urunFiyat;
    }

    public String getUrunResim() {
        return urunResim;
    }

    public void setUrunResim(String urunResim) {
        this.urunResim = urunResim;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public int getUrunAdet() {
        return urunAdet;
    }

    public void setUrunAdet(int urunAdet) {
        this.urunAdet = urunAdet;
    }

    public double getUrunFiyat() {
        return urunFiyat;
    }

    public void setUrunFiyat(double urunFiyat) {
        this.urunFiyat = urunFiyat;
    }
}
