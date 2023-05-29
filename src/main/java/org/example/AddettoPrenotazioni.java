package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AddettoPrenotazioni {

    private static void creaPrenotazione(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("In che giorno vorresti aggiungere la prenotazione? (G/M/A) ");
        String data = scanner.next();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate d = LocalDate.parse(data, formatter); // converto da stringa a oggetto LocalDate
        System.out.print("Numero di coperti della prenotazione? ");
        int coperti = scanner.nextInt();
        double cdl = 0.0;
        String output;
        HashMap<String, Integer> richiesta = new HashMap<>();
        int controllo=0;
        do{
            System.out.print("\nMENU ADDETTO ALLE PRENOTAZIONI\ni  -> inserisci il nome di un piatto/menu tematico " +
                    "\nm -> visualizza lista menu tematici\np -> visualizza lista piatti\nq -> termina\n");
            output = scanner.next();
            switch (output.toLowerCase().charAt(0)) {
                case 'm':
                    Gestore.visualizzaMenuTematici(data);
                    break;
                case 'p':
                    Gestore.visualizzaPiattiDisponibili(data);
                    break;
                case 'i':
                    String nome;
                    do{
                        System.out.print("Inserisci il nome di un piatto/menu tematico: (q -> termina)");
                        nome = scanner.next();
                        // check se esiste un menu o un piatto inserito
                        if(Gestore.cercaPiatto(nome) || Gestore.cercaMenuTematico(nome)){
                            // Prendi il carico di lavoro del piatto o del menu
                            if(Gestore.cercaPiatto(nome)) cdl += Gestore.getCdlPiatto(nome);
                            if(Gestore.cercaMenuTematico(nome)) cdl += Gestore.getCdlMenu(nome);
                            System.out.print("Numero di piatti o menu da ordinare? ");
                            int n = scanner.nextInt();
                            richiesta.put(nome, n);
                            controllo+=n;
                        }
                    }while(!(nome.toLowerCase().equals("q")));
                    break;
                case 'q':
                    // esci dal menu
                    break;
                default:
                    System.out.print("Scegli una delle opzioni!");
            }
        }while(!(output.equals("q")));
        // Prima di inserire la prenotazione devo verificare le proprietà
        // 1 - Tutti le persone devono ordinare qualcosa
        if(controllo>=coperti){
            // 2 - Controllo posti disponibili
            if(checkPostiPrenotazioni(data, coperti)){
                if(checkCdlPrenotazioni(data, cdl)){
                    Prenotazione prenotazione = new Prenotazione();
                    prenotazione.setData(d);
                    prenotazione.setNumeroCoperti(coperti);
                    prenotazione.setRichiesta(richiesta);
                    prenotazione.setCaricoDiLavoro(cdl);
                    prenotazione.salvaPrenotazione();
                } else {
                    System.out.print("PRENOTAZIONE FALLITA!\nil carido di lavoro del ristorante viene superato");
                }

            } else {
                System.out.print("PRENOTAZIONE FALLITA!\nil ristorante è pieno nella data selezionata");
            }
        } else {
            System.out.print("La somma dei valori numero persone relativi a tutte le coppie di una prenotazione " +
                    "deve essere maggiore o uguale al valore numero coperti dell’ordinazione stessa.");
        }
        System.out.print("Prenotazione effettuata con successo! ");


    }

    public static @NotNull List<String> getPrenotazioniDelGiorno(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> listaPrenotazioni = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("prenotazioni.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                LocalDate dataChek = LocalDate.parse(riga[1], formatter);
                LocalDate now = LocalDate.now();
                if (dataChek.equals(now)){
                    listaPrenotazioni.add(riga[2]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPrenotazioni;
    }

    public static int getCopertiDelGiorno() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int coperti = 0;
        try (CSVReader reader = new CSVReader(new FileReader("prenotazioni.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                LocalDate dataChek = LocalDate.parse(riga[1], formatter);
                LocalDate now = LocalDate.now();
                if (dataChek.equals(now)){
                   coperti+=Integer.parseInt(riga[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coperti;
    }

    // prendo in input la data della prenotazione e il numero di coperti, controllo il numero di coperti occupati
    // già per quella data e se c'è posto anche per la nuova prenotazione ritorna true altrimenti falso.
    private static boolean checkPostiPrenotazioni(String d, int x){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(d, formatter); // converto da stringa a oggetto LocalDate
        int copertiOccupati = 0;
        try (CSVReader reader = new CSVReader(new FileReader("prenotazioni.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                LocalDate dataChek = LocalDate.parse(riga[1], formatter);
                if(dataChek.equals(data)){
                    copertiOccupati += Integer.parseInt(riga[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Gestore.posti >= (copertiOccupati+x)) return true;
        else return false;
    }

    private static boolean checkCdlPrenotazioni(String d, double x){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(d, formatter); // converto da stringa a oggetto LocalDate
        double cdlTotale = 0;
        try (CSVReader reader = new CSVReader(new FileReader("prenotazioni.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                LocalDate dataChek = LocalDate.parse(riga[1], formatter);
                if(dataChek.equals(data)){
                    cdlTotale += Double.parseDouble(riga[3]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Gestore.caricoSostenibileRistorante >= (cdlTotale + x)) return true;
        else return false;
    }

    private static void rimozionePrenotazioniPassate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String[]> fileAggiornato = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("prenotazioni.csv"))) {
            String[] intestazione = reader.readNext();
            fileAggiornato.add(intestazione);
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                LocalDate dataChek = LocalDate.parse(riga[1], formatter);
                LocalDate now = LocalDate.now();
                if (dataChek.isAfter(now) || dataChek.equals(now)){
                    fileAggiornato.add(riga);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File prenotazioni = new File("prenotazioni.csv");
        prenotazioni.delete();
        try(CSVWriter writer = new CSVWriter(new FileWriter("prenotazioni.csv"))){
            for(int i=0; i<fileAggiornato.size(); i++){
                writer.writeNext(fileAggiornato.get(i), false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {

        creaPrenotazione();
        rimozionePrenotazioniPassate();

    }


}
