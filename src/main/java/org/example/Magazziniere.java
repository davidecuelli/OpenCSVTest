package org.example;

import java.util.List;

public class Magazziniere {

    private static void creaListaSpesa(){
        List<String> listaRichiesta = AddettoPrenotazioni.getPrenotazioniDelGiorno();
        for (String richiesta:listaRichiesta) {
            System.out.print(richiesta);
        }
    }

    public static void main(String[] args) {

        creaListaSpesa();


    }

}
