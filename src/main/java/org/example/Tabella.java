/*
Leggere file CSV e trasfomarlo in una lista di oggetti in questo caso Persona
 */
package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tabella {

    public static void main(String[] args) {

        String filePath = "tabella.csv";

        // scrittura (nel fileWriter oltre al filePath aggiungo un true che permette di fare Append e non sovrascrivere
        try(CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))){

            String test = "dad";
            String[] row1 = {test, "Rossi", "30"};
            writer.writeNext(row1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // lettura
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {

            List<Persona> listaPersone = new ArrayList<>();

            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();

            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                // Crea un oggetto Persona per ogni riga
                Persona persona = new Persona();
                persona.setNome(riga[0]);
                persona.setCognome(riga[1]);
                persona.setEta(Integer.parseInt(riga[2]));
                listaPersone.add(persona);
            }

            // Stampa la lista di persone
            for (Persona persona : listaPersone) {
                System.out.println(persona);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Persona {
        private String nome;
        private String cognome;
        private int eta;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCognome() {
            return cognome;
        }

        public void setCognome(String cognome) {
            this.cognome = cognome;
        }

        public int getEta() {
            return eta;
        }

        public void setEta(int eta) {
            this.eta = eta;
        }

        @Override
        public String toString() {
            return "Persona{" +
                    "nome='" + nome + '\'' +
                    ", cognome='" + cognome + '\'' +
                    ", eta=" + eta +
                    '}';
        }
    }
}
