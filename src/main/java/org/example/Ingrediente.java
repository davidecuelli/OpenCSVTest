package org.example;

import java.util.ArrayList;

public class Ingrediente extends ArrayList<Ingrediente> {

    private String name;
    private String misura;


    public Ingrediente(){
    }

    public Ingrediente(String name, String misura) {
        this.name = name;
        this.misura = misura;
    }

    public String getName() {
        return name;
    }

    public String getMisura() {
        return misura;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "name='" + name + '\'' +
                ", misura='" + misura + '\'' +
                '}';
    }
}
