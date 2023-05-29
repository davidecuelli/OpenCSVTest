package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Magazziniere {

    public static void azzeraListaSpesa(){
        File spesa = new File("listaspesa.csv");
        spesa.delete();
        FileWriter newSpesa = null;
        try {
            newSpesa = new FileWriter("listaspesa.csv");
            newSpesa.write("merce,quantità,unitàDiMisura\n");
            newSpesa.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static void creaListaSpesa(){
        azzeraListaSpesa();

        List<String> listaRichiesta = AddettoPrenotazioni.getPrenotazioniDelGiorno();
        HashMap<String,Integer> ricette = new HashMap<>();
        for (String richiesta:listaRichiesta) {
            richiesta = richiesta.replaceAll("[{}]", "");
            String[] splitRichiesta = richiesta.split(",");
            // dividi le stringhe e crea una hash map nome e qt

            for (int i=0;i<splitRichiesta.length;i++){
                // qui devo gestire una stringa del tipo "nome=1" cioè menu o piatto e la qt
                splitRichiesta[i]=splitRichiesta[i].replaceAll(" ", "");
                String[] dati = splitRichiesta[i].split("=");
                if(Gestore.cercaPiatto(dati[0])){
                    if(ricette.containsKey(dati[0])){
                        ricette.merge(dati[0], Integer.parseInt(dati[1]), Integer::sum);
                    } else {
                        ricette.put(dati[0],Integer.parseInt(dati[1]));
                    }
                }
                if(Gestore.cercaMenuTematico(dati[0])) {
                    for (String str : Gestore.getListaPiattiMenu(dati[0])) {
                        if(ricette.containsKey(str)){
                            ricette.merge(str, Integer.parseInt(dati[1]), Integer::sum);
                        } else {
                            ricette.put(str,Integer.parseInt(dati[1]));
                        }
                    }
                }
            }
        }
        // System.out.println("test: " + ricette.toString()); debug
        // scorro l'hashmap e prendo gli ingredienti necessari
        for (Map.Entry<String, Integer> entry : ricette.entrySet()){
            String nome = entry.getKey();
            Integer qt = entry.getValue();
            try (CSVReader reader = new CSVReader(new FileReader("ricette.csv"))) {
                String[] intestazione = reader.readNext();
                String[] riga;
                while ((riga = reader.readNext()) != null) {
                    if(nome.equals(riga[0])){
                        String[] splitSprings = riga[2].split("\\)\\(");
                        List<String> resultStrings = new ArrayList<>();
                        for (String s : splitSprings){
                            resultStrings.add(s.replace(")","").replace("(",""));
                        }
                        for (String s : resultStrings){
                            String[] dati = s.split(" ");
                            // System.out.println(dati[0] + dati[1] + dati[2] + ""); debug
                            try(CSVWriter writer = new CSVWriter(new FileWriter("listaspesa.csv", true))){
                                int qtAggiornata = Integer.parseInt(dati[0])*qt;
                                String[] row1 = {dati[2], qtAggiornata+"", dati[1]};
                                writer.writeNext(row1, false);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        rimuoviDoppieListaSpesa();
        aggiungiBevandeGeneriExtra();

        visualizzaListaSpesa();
    }

    private static void visualizzaRegistroMagazzino(){

        // NB: metti apposto la funzione aggiungiBevandeGeneriExtra, poichè aggiunge tutti i generi extra a
        // prescindere delle prenotazioni. Bisogna contare il numero di coperti per il giorno successivo e
        // in base a quanti sono agire di conseguenzax

        // PARTE 1.
        // controllo nella lista della spesa. Dopo vedo se gli articoli nella lista sono già presenti nel
        // magazzino altrimenti gli aggiungo. Se sono già presenti ne modifico le qt.


        // PARTE 2.
        // Devo togliere le qt usate nelle ricette ed eventuali ingredienti persi/scaduti/rovinati nella preparazione.
        // già nella lista della spesa richiedo tutti gli ingredienti che servono per le prenotazioni del giorno.
        // L'incremento massimo possibile sia del 10% -> acquistare il 10% in più per sicurezza.


    }

    private static void aggiungiBevandeGeneriExtra() {
        int coperti = AddettoPrenotazioni.getCopertiDelGiorno();
        try (CSVReader reader = new CSVReader(new FileReader("bevande.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                try(CSVWriter writer = new CSVWriter(new FileWriter("listaspesa.csv", true))){
                    String[] row1 = {riga[0], riga[1], "litri"};
                    writer.writeNext(row1, false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (CSVReader reader = new CSVReader(new FileReader("generiextra.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                try(CSVWriter writer = new CSVWriter(new FileWriter("listaspesa.csv", true))){
                    double qt = Double.parseDouble(riga[1])*coperti;
                    String[] row1 = {riga[0], qt+"", "grammi"};
                    writer.writeNext(row1, false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void visualizzaListaSpesa() {
        System.out.println("------------ Lista della Spesa ------------");
        try (CSVReader reader = new CSVReader(new FileReader("listaspesa.csv"))) {
            String[] intestazione = reader.readNext();
            System.out.println(intestazione[0] + " | " + intestazione[1] + " | " + intestazione[2]);
            String[] riga;
            int count=0;
            while ((riga = reader.readNext()) != null) {
                System.out.println(riga[0] + " | " + riga[1] + " | " + riga[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------------------");
    }

    private static void rimuoviDoppieListaSpesa(){
        HashMap<String, Integer> checkQt = new HashMap<>();
        HashMap<String, String> checkMisura = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader("listaspesa.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            int count=0;
            while ((riga = reader.readNext()) != null) {
                if(checkQt.containsKey(riga[0])){
                    checkQt.merge(riga[0], Integer.parseInt(riga[1]), Integer::sum);
                } else {
                    checkQt.put(riga[0],Integer.parseInt(riga[1]));
                    checkMisura.put(riga[0],riga[2]);
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(checkQt);
        //System.out.println(checkMisura);
        azzeraListaSpesa();

        for (Map.Entry<String, Integer> entry : checkQt.entrySet()) {
            String nome = entry.getKey();
            Integer qt = entry.getValue();
            String misura = checkMisura.get(nome);
            try (CSVWriter writer = new CSVWriter(new FileWriter("listaspesa.csv", true))) {
                String[] row1 = {nome, qt + "", misura};
                writer.writeNext(row1, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) {

        creaListaSpesa();
        //aggiungiBevandeGeneriExtra();


    }

}
