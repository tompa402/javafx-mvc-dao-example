package hr.java.vjezbe.application;

import hr.java.vjezbe.model.Model;
import hr.java.vjezbe.model.RadioSondazna;
import hr.java.vjezbe.model.Senzor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static Model model;

    public Main() {
    }

    public static void main(String[] args) {
        model = new Model();
        ispisiPostaje();
    }

    private static void ispisiPostaje() {
        model.getPostajaService().findAll().forEach(postaja -> {
            System.out.println("--------------------");
            if (postaja instanceof RadioSondazna) {
                System.out.println("Postaja je radio sondažna");
                System.out.println("Visina radio sondažne mjerne postaje: " + ((RadioSondazna) postaja).dohvatiVisinuPostaje());
            }
            System.out.println("Naziv mjerne postje: " + postaja.getNaziv());
            System.out.println("Postaja se nalazi u mjestu " + postaja.getMjesto().getNaziv() + " (" + postaja.getMjesto().getVrstaMjesta().name() + "), županiji " + postaja.getMjesto().getZupanija().getNaziv() + ", državi " + postaja.getMjesto().getZupanija().getDrzava().getNaziv());
            System.out.println("Točne koordinate postaje su x:" + postaja.getGeografskaTocka().getGeoX() + " y:" + postaja.getGeografskaTocka().getGeoY());
            System.out.println("Vrijednosti senzora postaje su:");
            for (Senzor senzor : postaja.getSenzori()) {
                System.out.println(senzor.dohvatiPodatkeSenzora());
            }
            System.out.println();
        });
    }
}
