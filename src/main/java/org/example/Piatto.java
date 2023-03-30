package org.example;/*
CLASSE PIATTO
ha una denominazione precisa, stabilita dal gestore e destinata al consumatore, cioè tale denominazione è quella
che compare nei menu. Può non essere disponibile per tutto l'arco dell'anno. Corrisponde a una ricetta, quella usata
per cucinarlo, questa corrispondenza è ad uso degli addetti ai lavori entro il ristorante mentre è invisibile
ai clienti. Inoltre è dotato di una carico di lavoro che, per definizione, coinide col carico di lavoro per
porzione indicato nella suddetta ricetta.
*/
import java.time.LocalDateTime;
import java.time.Month;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Piatto {

    private String nome; // corrisponde al nome della ricetta
    private String denominazione;
    private double caricoDiLavoro; // coincide col carico di lavoro per porzione indicato nella ricetta

    // Se non sono state settate disponibilità sarà disponibile tutto l'anno
    private String dataInizioDisponibilità; // " giorno / mese / anno " -> formato della data
    private String dataFineDisponibilità;

    public Piatto(){}

    public Piatto(String nome, String denominazione, double caricoDiLavoro) {
        this.nome = nome;
        this.denominazione = denominazione;
        this.caricoDiLavoro = caricoDiLavoro;
    }

    public Piatto(String nome, String denominazione, double caricoDiLavoro, String dataInizioDisponibilità, String dataFineDisponibilità) {
        this.nome = nome;
        this.denominazione = denominazione;
        this.caricoDiLavoro = caricoDiLavoro;
        this.dataInizioDisponibilità = dataInizioDisponibilità;
        this.dataFineDisponibilità = dataFineDisponibilità;
    }

    @Override
    public String toString() {
        return "Piatto{" +
                "nome='" + nome + '\'' +
                ", denominazione='" + denominazione + '\'' +
                ", caricoDiLavoro=" + caricoDiLavoro +
                ", dataInizioDisponibilità='" + dataInizioDisponibilità + '\'' +
                ", dataFineDisponibilità='" + dataFineDisponibilità + '\'' +
                '}';
    }



    public boolean piattoDisponibile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInizio = LocalDate.parse(dataInizioDisponibilità, formatter);
        LocalDate dataFine = LocalDate.parse(dataFineDisponibilità, formatter);
        LocalDate oggi = LocalDate.now();
        return oggi.isEqual(dataInizio) || oggi.isEqual(dataFine) || (oggi.isAfter(dataInizio) && oggi.isBefore(dataFine));
    }


/*
    // Metodo che ritorna true se la data di oggi è compresa tra la disponbilità del piatto altrimenti false
    public boolean piattoDisponibile() {
        LocalDateTime dateTime = LocalDateTime.now();
        int anno = dateTime.getYear();
        Month m = dateTime.getMonth();
        int mese = m.getValue();
        int giorno = dateTime.getDayOfMonth();
        String partiDataInizio[] = dataInizioDisponibilità.split("/");
        String partiDataFine[] = dataFineDisponibilità.split("/");
        // ora controllo che la data odierna sia compresa tra le due date
        if((Integer.parseInt(partiDataInizio[2]) <= anno && Integer.parseInt(partiDataFine[2]) >= anno) &&
                (Integer.parseInt(partiDataInizio[1]) <= mese && Integer.parseInt(partiDataFine[1]) >= mese) &&
                (Integer.parseInt(partiDataInizio[0]) <= giorno && Integer.parseInt(partiDataFine[0]) >= giorno))
            return true;
        else
            return false;
    }
*/

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public double getCaricoDiLavoro() {
        return caricoDiLavoro;
    }

    public void setCaricoDiLavoro(double caricoDiLavoro) {
        this.caricoDiLavoro = caricoDiLavoro;
    }

    public String getDataInizioDisponibilità() {
        return dataInizioDisponibilità;
    }

    public void setDataInizioDisponibilità(String dataInizioDisponibilità) {
        this.dataInizioDisponibilità = dataInizioDisponibilità;
    }

    public String getDataFineDisponibilità() {
        return dataFineDisponibilità;
    }

    public void setDataFineDisponibilità(String dataFineDisponibilità) {
        this.dataFineDisponibilità = dataFineDisponibilità;
    }
}
