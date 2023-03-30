package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

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

public class Main {

    private static int caricoDiLavoro = 1; // inizializzato a 1, può essere modificato
    private int posti = 50; // inizializzo i posti a sedere del Ristorante
    private double caricoSostenibileRistorante = caricoDiLavoro*posti*1.2;

    public void modificaCaricoDiLavoro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il carico di lavoro (in minuti): ");
        int x = scanner.nextInt();
        caricoDiLavoro = x;
    }

    public void modificaPosti() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il numero di posti a sedere: ");
        int x = scanner.nextInt();
        caricoDiLavoro = x;
    }

    public static void inserisciIngrediente(String nome, String misura){
        // scrittura (nel fileWriter oltre al filePath aggiungo un true che permette di fare Append e non sovrascrivere
        try(CSVWriter writer = new CSVWriter(new FileWriter("ingredienti.csv", true))){

            String[] row1 = {nome, misura};
            writer.writeNext(row1, false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void inserisciRicetta(){
        Scanner scanner = new Scanner(System.in);
        // CREAZIONE DI UNA RICETTA
        System.out.print("Come vuoi chiamare la ricetta?");
        String nome = scanner.next();
        System.out.print("Quante porzioni avrà la ricetta?");
        String strPorzioni = scanner.next();
        int porzioni = Integer.parseInt(strPorzioni);
        System.out.print("Quanti ingredienti avrà la tua ricetta?");
        String strCount = scanner.next();
        int count = Integer.parseInt(strCount);
        /*
         Inserisci gli ingredienti uno per uno. Per ogni ingrediente controlla che sia già salvato
         su file, se è così allora prendi quell'ingrediente e inserisci la dose opportuna.
         Altrimenti se non è presente inseriscilo nella lista degli ingredienti.
         */
        System.out.println("Inserisci tutti gli ingredienti della ricetta \n");
        HashMap<Ingrediente, Integer> ingredienti = new HashMap<Ingrediente, Integer>();
        File fileIngredienti = new File("ingredienti.csv");
        String strIngredientiFinal = "";
        String strIngredienti = "";
        for(int i=0; i<count; i++){
            System.out.print("Nome ingrediente:");
            String nomeIngrediente = scanner.next();
            // cerca suò file ingredienti csv se è già presente nella lista ingredienti
            boolean presente = false;
            try (CSVReader reader = new CSVReader(new FileReader("ingredienti.csv"))) {

                List<Ingrediente> listaIngredienti = new ArrayList<>();

                // Leggi la prima riga (intestazione)
                String[] intestazione = reader.readNext();

                // Leggi le righe successive (dati)
                String[] riga;
                while ((riga = reader.readNext()) != null) {
                    if(nomeIngrediente.equals(riga[0])) {
                        strIngredienti = " " + riga[1] + " " + riga[0] + ")";
                        presente = true;
                    }
                }
                if (presente == false){
                    System.out.print("Unità di misura:");
                    String misura = scanner.next();
                    strIngredienti = " " + misura + " " + nomeIngrediente + ")";
                    inserisciIngrediente(nomeIngrediente,misura); // inserisci ingrediente

                } else
                    presente=true;
                System.out.print("Dose ingrediente:");
                String doseIngrediente = scanner.next();
                strIngredientiFinal += "(" + doseIngrediente + strIngredienti;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(strIngredientiFinal);
        // scrittura (nel fileWriter oltre al filePath aggiungo un true che permette di fare Append e non sovrascrivere
        try(CSVWriter writer = new CSVWriter(new FileWriter("ricette.csv", true))){

            String[] row1 = {nome, porzioni + "", strIngredientiFinal};
            writer.writeNext(row1, false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void visualizzaRicettario(){
        // lettura
        try (CSVReader reader = new CSVReader(new FileReader("ricette.csv"))) {

            List<Ricetta> listaRicette = new ArrayList<>();

            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();

            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                Ricetta ricetta = new Ricetta();
                ricetta.setNome(riga[0]);
                ricetta.setPorzioni(Integer.parseInt(riga[1]));
                String[] ingredienti = riga[2].split("\\)");
                HashMap<Ingrediente, Integer> hashIngredienti = new HashMap<>();
                for (int i = 0; i < ingredienti.length; i++) {
                    ingredienti[i] = ingredienti[i].replace("(", "").replace(")", "");
                    String[] ingrediente = ingredienti[i].split(" ");
                    Ingrediente in = new Ingrediente(ingrediente[1], ingrediente[2]);
                    hashIngredienti.put(in, Integer.parseInt(ingrediente[0]));
                }
                ricetta.setIngredienti(hashIngredienti);
                listaRicette.add(ricetta);
            }

            // Stampa la lista di persone
            for (Ricetta ricetta : listaRicette) {
                System.out.println(ricetta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean cercaRicetta(String x){

        try (CSVReader reader = new CSVReader(new FileReader("ricette.csv"))) {

            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();

            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if(x.equals(riga[0])) return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static double getCaricoDiLavoroPerPorzione(String x){

        try (CSVReader reader = new CSVReader(new FileReader("ricette.csv"))) {

            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();

            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if (x.equals(riga[0])) {
                    return 1/(Integer.parseInt(riga[1]));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static void inserisciPiatto() {
        Scanner scanner = new Scanner(System.in);
        // CREAZIONE DI UNA RICETTA
        String nome;
        do{
            System.out.print("Inserisci il nome di una ricetta: ");
            nome = scanner.next();
        }while(cercaRicetta(nome) == false);
        System.out.print("Inserisci la denominazione per il piatto: ");
        String denominazione = scanner.next();
        double cdl = getCaricoDiLavoroPerPorzione(nome);
        System.out.print("La ricetta è disponibile tutto l'anno? (S/N)");
        String risposta = scanner.next();
        System.out.print("");
        switch (risposta.toLowerCase().charAt(0)){
            case 's':
                try(CSVWriter writer = new CSVWriter(new FileWriter("piatti.csv", true))){

                    String[] row1 = {nome, denominazione,cdl+"","",""};
                    writer.writeNext(row1, false);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 'n':
                System.out.print("Inserisci la data di inizio disponibilità? (G/M/A)");
                String dataInizio = scanner.next();
                System.out.print("Inserisci la data di fine disponibilità? (G/M/A)");
                String dataFine = scanner.next();
                try(CSVWriter writer = new CSVWriter(new FileWriter("piatti.csv", true))){

                    String[] row1 = {nome, denominazione,cdl+"",dataInizio,dataFine};
                    writer.writeNext(row1, false);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                System.out.print("Inserisci una risposta valida");

        }

    }

    private static void visualizzaPiatti(){
        try (CSVReader reader = new CSVReader(new FileReader("piatti.csv"))) {

            List<Piatto> listaPiatti = new ArrayList<>();

            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();

            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                Piatto piatto = new Piatto();
                piatto.setNome(riga[0]);
                piatto.setDenominazione(riga[1]);
                piatto.setCaricoDiLavoro(Double.parseDouble(riga[2]));
                if(riga[3].isEmpty() && riga[4].isEmpty()){
                    listaPiatti.add(piatto);
                }else{
                    piatto.setDataInizioDisponibilità(riga[3]);
                    piatto.setDataFineDisponibilità(riga[4]);
                    listaPiatti.add(piatto);
                }

            }
            // Stampa la lista piatti
            for (Piatto piatto : listaPiatti) {
                System.out.println(piatto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void visualizzaPiattiDisponibili(){
        try (CSVReader reader = new CSVReader(new FileReader("piatti.csv"))) {

            List<Piatto> listaPiatti = new ArrayList<>();

            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();

            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                Piatto piatto = new Piatto();
                piatto.setNome(riga[0]);
                piatto.setDenominazione(riga[1]);
                piatto.setCaricoDiLavoro(Double.parseDouble(riga[2]));
                if(riga[3].isEmpty() && riga[4].isEmpty()){
                    listaPiatti.add(piatto);
                }else{
                    piatto.setDataInizioDisponibilità(riga[3]);
                    piatto.setDataFineDisponibilità(riga[4]);
                    if(piatto.piattoDisponibile()){
                        listaPiatti.add(piatto);
                    }
                }

            }

            // Stampa la lista piatti disponibili
            for (Piatto piatto : listaPiatti) {
                System.out.println(piatto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Piatto getPiatto(String x){

        try (CSVReader reader = new CSVReader(new FileReader("piatti.csv"))) {

            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();

            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                Piatto piatto = new Piatto();
                piatto.setNome(riga[0]);
                piatto.setDenominazione(riga[1]);
                piatto.setCaricoDiLavoro(Double.parseDouble(riga[2]));
                if(!(riga[3].isEmpty() && riga[4].isEmpty())){
                    piatto.setDataInizioDisponibilità(riga[3]);
                    piatto.setDataFineDisponibilità(riga[4]);
                }
                if(x.equals(riga[0])) return piatto;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void creaMenuTematico(){
        Scanner scanner = new Scanner(System.in);
        List<Piatto> piatti = new ArrayList<>();
        System.out.print("Inserisci un nome per il Menu Tematico");
        String nome = scanner.next();
        String output;
        do{
            System.out.print("Inserisci il nome di un piatto da inserire nel menu: " +
                    "\nv -> visualizza lista piatti\nq -> termina");
            output = scanner.next();
            if(output.equals("v")){
                visualizzaPiatti();
            } else {
                if(!(output.equals("q"))) {
                    Piatto piatto = getPiatto(output);
                    if (piatto == null) {
                        System.out.print("Inserisci un nome valido");
                    } else {
                        piatti.add(piatto);
                    }
                }
            }
        }while(!(output.equals("q")));
        System.out.print("Il Menu Tematico è disponibile tutto l'anno? (S/N)");
        String risposta = scanner.next();
        System.out.print("");
        switch (risposta.toLowerCase().charAt(0)){
            case 's':
                try(CSVWriter writer = new CSVWriter(new FileWriter("menutematici.csv", true))){
                    MenuTematico menuTematico = new MenuTematico(nome, piatti);
                    menuTematico.setCaricoLavoro(menuTematico.getCaricoLavoro());
                    if(menuTematico.caricoLavoroValido(caricoDiLavoro)){
                        String[] row1 = {menuTematico.getNome(), menuTematico.getPiattiToString(),
                                menuTematico.getCaricoLavoro()+"","",""};
                        writer.writeNext(row1, false);
                    } else {
                        System.out.print("carico di lavoro del menu non valido, menu NON CREATO");
                    }


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 'n':
                System.out.print("Inserisci la data di inizio disponibilità? (G/M/A)");
                String dataInizio = scanner.next();
                System.out.print("Inserisci la data di fine disponibilità? (G/M/A)");
                String dataFine = scanner.next();
                try(CSVWriter writer = new CSVWriter(new FileWriter("piatti.csv", true))){
                    MenuTematico menuTematico = new MenuTematico(nome, piatti, dataInizio, dataFine);
                    menuTematico.setCaricoLavoro(menuTematico.getCaricoLavoro());
                    if(menuTematico.caricoLavoroValido(caricoDiLavoro)){
                        String[] row1 = {menuTematico.getNome(), menuTematico.getPiattiToString(),
                                menuTematico.getCaricoLavoro()+"",menuTematico.getDataInizio(),menuTematico.getDataFine()};
                        writer.writeNext(row1, false);
                    } else {
                        System.out.print("carico di lavoro del menu non valido, menu NON CREATO");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                System.out.print("Inserisci una risposta valida");
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("GESTORE");

        //inserisciRicetta();

        //visualizzaRicettario();

        //inserisciPiatto();

        //visualizzaPiattiDisponibili();

        creaMenuTematico();

    }



}