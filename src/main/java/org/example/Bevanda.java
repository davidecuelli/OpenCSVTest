package org.example;

public class Bevanda {

    private String nome;
    private double consumoProCapite;

    public Bevanda(){}

    public Bevanda(String nome, double consumoProCapite) {
        this.nome = nome;
        this.consumoProCapite = consumoProCapite;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getConsumoProCapite() {
        return consumoProCapite;
    }

    public void setConsumoProCapite(double consumoProCapite) {
        this.consumoProCapite = consumoProCapite;
    }

    @Override
    public String toString() {
        return "Bevanda{" +
                "nome='" + nome + '\'' +
                ", consumoProCapite=" + consumoProCapite +
                '}';
    }
}
