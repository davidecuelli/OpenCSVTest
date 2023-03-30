package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuTematico {
    private String nome;
    private List<Piatto> piatti;
    private String dataInizio; // " giorno / mese / anno " -> formato della data
    private String dataFine;
    private double caricoLavoro;

    public MenuTematico(String nome, List<Piatto> piatti) {
        this.nome = nome;
        this.piatti = piatti;
    }

    public MenuTematico(String nome, List<Piatto> piatti, String dataInizio, String dataFine) {
        this.nome = nome;
        this.piatti = piatti;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public void aggiungiPiatto(Piatto piatto) {
        piatti.add(piatto);
    }

    public double getCaricoLavoro() {
        double caricoTotale = 0;
        for (Piatto piatto : piatti) {
            caricoTotale += piatto.getCaricoDiLavoro();
        }
        return caricoTotale;
    }

    public boolean isValido() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInizio = LocalDate.parse(getDataInizio(), formatter);
        LocalDate dataFine = LocalDate.parse(getDataFine(), formatter);
        LocalDate oggi = LocalDate.now();
        return oggi.isEqual(dataInizio) || oggi.isEqual(dataFine) || (oggi.isAfter(dataInizio) && oggi.isBefore(dataFine));
    }

    public boolean caricoLavoroValido(int caricoLavoroPersona) {
        return getCaricoLavoro() <= (4.0/3.0) * caricoLavoroPersona;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Piatto> getPiatti() {
        return piatti;
    }

    public void setPiatti(List<Piatto> piatti) {
        this.piatti = piatti;
    }

    public String getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public void setCaricoLavoro(double caricoLavoro) {
        this.caricoLavoro = caricoLavoro;
    }

    public String getPiattiToString() {
        String output="";
        for (Piatto piatto : piatti) output += piatto.getNome() + "+";
        output = output.substring(0, output.length() - 1);
        return output;
    }
}

