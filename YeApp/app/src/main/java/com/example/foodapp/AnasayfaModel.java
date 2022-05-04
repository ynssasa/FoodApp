package com.example.foodapp;

public class AnasayfaModel {

    String langLogo;
    String langLogoArka;
    String langName;
    boolean tiklama=false;

    public AnasayfaModel(String langLogo, String langLogoArka, String langName){
        this.langLogo=langLogo;
        this.langName=langName;
        this.langLogoArka=langLogoArka;
    }

    public String getLangLogo() {
        return langLogo;
    }

    public String getLangName() {
        return langName;
    }

    public String getLangLogoArka() {
        return langLogoArka;
    }

    public boolean isTiklama() {
        return tiklama;
    }

}