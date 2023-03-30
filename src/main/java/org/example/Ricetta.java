package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class Ricetta extends ArrayList<Ricetta> {

    private String nome;
    private HashMap<Ingrediente, Integer> ingredienti;
    private int porzioni;

    public Ricetta(){
    }

    public Ricetta(String nome, HashMap<Ingrediente, Integer> ingredienti, int porzioni) {
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.porzioni = porzioni;
    }

    @Override
    public String toString() {
        return "Ricetta{" +
                "nome='" + nome + '\'' +
                ", ingredienti=" + ingredienti +
                ", porzioni=" + porzioni +
                '}';
    }

    public double getCaricoDiLavoroPerPorzione()  {
        return 1/porzioni;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public HashMap<Ingrediente, Integer> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(HashMap<Ingrediente, Integer> ingredienti) {
        this.ingredienti = ingredienti;
    }

    public int getPorzioni() {
        return porzioni;
    }

    public void setPorzioni(int porzioni) {
        this.porzioni = porzioni;
    }
}
