package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Gestore {

    private static int caricoDiLavoro = 30; // inizializzato a 1, può essere modificato
    public static int posti = 50; // inizializzo i posti a sedere del Ristorante
    public static double caricoSostenibileRistorante = caricoDiLavoro*posti*1.2;

    public static double getCdlPiatto(String nome) {
        try (CSVReader reader = new CSVReader(new FileReader("piatti.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if(nome.equals(riga[0])) return Double.parseDouble(riga[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static double getCdlMenu(String nome) {
        try (CSVReader reader = new CSVReader(new FileReader("menutematici.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if(nome.equals(riga[0])) return Double.parseDouble(riga[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static List<String> getListaPiattiMenu(String nome) {
        List<String> list = null;
        try (CSVReader reader = new CSVReader(new FileReader("menutematici.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if(nome.equals(riga[0])) {
                    String[] array = riga[1].split("\\+");
                    list = Arrays.asList(array);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

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

    public static void visualizzaPiattiDisponibili(String x){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(x, formatter); // converto da stringa a oggetto LocalDate
        String output="";
        try (CSVReader reader = new CSVReader(new FileReader("piatti.csv"))) {
            List<Piatto> listaPiatti = new ArrayList<>();
            String[] intestazione = reader.readNext(); // Leggi la prima riga (intestazione)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                // controllo dataInizio e fine se non è presente stampo diretto.
                // Altrimenti controllo che il parametro data convertito in LocalDate sia compreso tra data inizio fine
                if(riga[3].equals("") || riga[4].equals("")){
                    output+=riga[0]+"\n";
                } else {
                    LocalDate dataInizio = LocalDate.parse(riga[3], formatter);
                    LocalDate dataFine = LocalDate.parse(riga[4], formatter);
                    if(data.isAfter(dataInizio) && data.isBefore(dataFine)){
                        output+=riga[0]+"\n";
                    }
                }
            }
            System.out.print(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean cercaPiatto(String x){
        try (CSVReader reader = new CSVReader(new FileReader("piatti.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if(x.equals(riga[0])) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static @Nullable Piatto getPiatto(String x){

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

    public static void visualizzaMenuTematici(String x){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(x, formatter); // converto da stringa a oggetto LocalDate
        String output="";
        try (CSVReader reader = new CSVReader(new FileReader("menutematici.csv"))) {
            List<MenuTematico> listaMenu = new ArrayList<>();
            String[] intestazione = reader.readNext(); // Leggi la prima riga (intestazione)
            String[] riga; // Leggi le righe successive (dati)
            while ((riga = reader.readNext()) != null) {
                // controllo dataInizio e fine se non è presente stampo diretto.
                // Altrimenti controllo che il parametro data convertito in LocalDate sia compreso tra data inizio fine
                if(riga[3].equals("") || riga[4].equals("")){
                    output+=riga[0]+"\n";
                } else {
                    LocalDate dataInizio = LocalDate.parse(riga[3], formatter);
                    LocalDate dataFine = LocalDate.parse(riga[4], formatter);
                    if(data.isAfter(dataInizio) && data.isBefore(dataFine)){
                        output+=riga[0]+"\n";
                    }
                }
            }
            System.out.print(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean cercaMenuTematico(String x){
        try (CSVReader reader = new CSVReader(new FileReader("menutematici.csv"))) {
            String[] intestazione = reader.readNext();
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if(x.equals(riga[0])) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void inserisciBevanda(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Come vuoi chiamare la bevande? ");
        String nome = scanner.next();
        System.out.print("Qual è il consumo pro capite della bevanda? (in litri) ");
        double consumoProCapite = scanner.nextDouble();
        try(CSVWriter writer = new CSVWriter(new FileWriter("bevande.csv", true))){

            String[] row1 = {nome, consumoProCapite+""};
            writer.writeNext(row1, false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void visualizzaBevande(){
        try (CSVReader reader = new CSVReader(new FileReader("bevande.csv"))) {
            List<Bevanda> listaBevande = new ArrayList<>();
            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();
            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                Bevanda bevanda = new Bevanda();
                bevanda.setNome(riga[0]);
                bevanda.setConsumoProCapite(Double.parseDouble(riga[1]));
                listaBevande.add(bevanda);
            }
            for (Bevanda bevanda : listaBevande) {
                System.out.println(bevanda);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void inserisciGenereExtra(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Come vuoi chiamare il genere extra? ");
        String nome = scanner.next();
        System.out.print("Qual è il consumo pro capite del genere extra? (in ettogrammi) ");
        double consumoProCapite = scanner.nextDouble();
        try(CSVWriter writer = new CSVWriter(new FileWriter("generiextra.csv", true))){

            String[] row1 = {nome, consumoProCapite+""};
            writer.writeNext(row1, false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void visualizzaGenereExtra(){
        try (CSVReader reader = new CSVReader(new FileReader("generiextra.csv"))) {
            List<GenereExtra> listaGeneriExtra = new ArrayList<>();
            // Leggi la prima riga (intestazione)
            String[] intestazione = reader.readNext();
            // Leggi le righe successive (dati)
            String[] riga;
            while ((riga = reader.readNext()) != null) {
                GenereExtra genereExtra = new GenereExtra();
                genereExtra.setNome(riga[0]);
                genereExtra.setConsumoProCapite(Double.parseDouble(riga[1]));
                listaGeneriExtra.add(genereExtra);
            }
            for (GenereExtra genereExtra : listaGeneriExtra) {
                System.out.println(genereExtra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("GESTORE");

        //inserisciRicetta();

        //visualizzaRicettario();

        //inserisciPiatto();

        //visualizzaPiattiDisponibili();

        //creaMenuTematico();

        //inserisciBevanda();

        //inserisciGenereExtra();

        visualizzaBevande();

        //visualizzaGenereExtra();

    }



}