package com.sbadc.survivalera.models;

public class ScreenItem {

    String Tittle,Description;
    int Screenimg;

    public ScreenItem(String tittle, String description, int screenimg) {
        Tittle = tittle;
        Description = description;
        Screenimg = screenimg;
    }

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String tittle) {
        Tittle = tittle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getScreenimg() {
        return Screenimg;
    }

    public void setScreenimg(int screenimg) {
        Screenimg = screenimg;
    }
}
