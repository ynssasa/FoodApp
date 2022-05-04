package com.example.foodapp;

public class SiparisUrun {
    private String siparisTarih;
    private String siparisResim;
    private String siparisId;
    private double siparisFiyat;
    private String siparisDetay;


    public SiparisUrun(String siparisResim, String siparisId, double siparisFiyat, String siparisTarih) {
        this.siparisResim = siparisResim;
        this.siparisId = siparisId;
        this.siparisFiyat = siparisFiyat;
        this.siparisTarih = siparisTarih;
        this.siparisDetay = "Detaylar ▶";
    }

    public String getSiparisDetay() {
        return siparisDetay;
    }

    public void setSiparisDetay(String siparisDetay) {
        this.siparisDetay = siparisDetay;
    }

    public String getSiparisTarih() {
        return siparisTarih;
    }

    public void setSiparisTarih(String siparisTarih) {
        this.siparisTarih = siparisTarih;
    }

    public String getSiparisResim() {
        return siparisResim;
    }

    public void setSiparisResim(String siparisResim) {
        this.siparisResim = siparisResim;
    }

    public String getSiparisId() {
        return siparisId;
    }

    public void setSiparisId(String siparisId) {
        this.siparisId = siparisId;
    }

    public double getSiparisFiyat() {
        return siparisFiyat;
    }

    public void setSiparisFiyat(double siparisFiyat) {
        this.siparisFiyat = siparisFiyat;
    }
}
