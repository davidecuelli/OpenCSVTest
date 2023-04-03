package org.example;

public class GenereExtra {

    private String nome;
    private double consumoProCapite;

    public GenereExtra(){}

    public GenereExtra(String nome, double consumoProCapite) {
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
        return "GenereExtra{" +
                "nome='" + nome + '\'' +
                ", consumoProCapite=" + consumoProCapite +
                '}';
    }
}
