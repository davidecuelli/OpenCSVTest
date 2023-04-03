package org.example;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Prenotazione {

    private int numeroCoperti;
    private LocalDate data;
    private HashMap<String, Integer> richiesta;
    private double caricoDiLavoro;

    public Prenotazione() {
    }

    public Prenotazione(int numeroCoperti, LocalDate data) {
        this.numeroCoperti = numeroCoperti;
        this.data = data;
    }

    public Prenotazione(int numeroCoperti, LocalDate data, HashMap<String, Integer> richiesta) {
        this.numeroCoperti = numeroCoperti;
        this.data = data;
        this.richiesta = richiesta;
    }

    public void salvaPrenotazione(){
        try(CSVWriter writer = new CSVWriter(new FileWriter("prenotazioni.csv", true))){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String data = getData().format(formatter);
            String[] row1 = {getNumeroCoperti() + "", data, getRichiesta().toString(), getCaricoDiLavoro()+""};
            writer.writeNext(row1, false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getCaricoDiLavoro() {
        return caricoDiLavoro;
    }

    public void setCaricoDiLavoro(double caricoDiLavoro) {
        this.caricoDiLavoro = caricoDiLavoro;
    }

    public int getNumeroCoperti() {
        return numeroCoperti;
    }

    public void setNumeroCoperti(int numeroCoperti) {
        this.numeroCoperti = numeroCoperti;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public HashMap<String, Integer> getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(HashMap<String, Integer> richiesta) {
        this.richiesta = richiesta;
    }
}
