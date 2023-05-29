package org.example;

import java.util.Map;

public class Magazzino {

    private String merce;
    private int qt; // quantità
    private String misura; // unità di misura

    public Magazzino(){}

    public Magazzino(String merce, int qt, String misura) {
        this.merce = merce;
        this.qt = qt;
        this.misura = misura;
    }

    public String getMerce() {
        return merce;
    }

    public void setMerce(String merce) {
        this.merce = merce;
    }

    public int getQt() {
        return qt;
    }

    public void setQt(int qt) {
        this.qt = qt;
    }

    public String getMisura() {
        return misura;
    }

    public void setMisura(String misura) {
        this.misura = misura;
    }
}
