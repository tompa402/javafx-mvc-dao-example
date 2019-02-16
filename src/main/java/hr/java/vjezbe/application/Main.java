package hr.java.vjezbe.application;

import hr.java.vjezbe.model.*;
import hr.java.vjezbe.model.generics.MjernePostaje;
import hr.java.vjezbe.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final int BROJ_POSTAJA = 2;
    private static final int BROJ_POSTAJA_SONDAZNE = 1;

    private static Model model;
    private static MjernePostaje<MjernaPostaja> postaje = new MjernePostaje<>();

    public Main() {
    }

    public static void main(String[] args) {
        model = new Model();
        model.getPostajaService().findAll().forEach(postaja -> postaje.add(postaja));

        /*Scanner scanner = new Scanner(System.in);
        System.out.println("UNOS PODATAKA:");

        for (int i = 0; i < BROJ_POSTAJA + BROJ_POSTAJA_SONDAZNE; i++) {
            boolean isSondazna = false;
            if (i >= BROJ_POSTAJA) {
                isSondazna = true;
            }
            System.out.println(String.format("Unesite %d. mjernu postaju:", i + 1));
            LOGGER.info("Unos " + i + ". mjerne postaje");
            postaje.add(unesiPostaju(scanner, isSondazna));
        }*/
        ispisiPostaje();
    }

    private static void ispisiPostaje() {
        postaje.getSortedList().forEach(postaja -> {
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

    private static MjernaPostaja unesiPostaju(Scanner scanner, boolean isSondazna) {
        System.out.println("Unesite naziv mjerne postaje:");
        String naziv = scanner.nextLine();

        Integer visina = null;
        if (isSondazna) {
            System.out.println("Unesite visinu radio sondažne mjerne postaje:");
            visina = Validator.isInt(scanner);
            scanner.nextLine();
        }

        Mjesto mjesto = unesiMjesto(scanner);
        Optional<MjernaPostaja> postaja = postaje.getSortedList().stream().filter(p ->
                p.getNaziv().equals(naziv)).findFirst();

        if (postaja.isPresent()) {
            return postaja.get();
        }

        MjernaPostaja mjernaPostaja;
        if (isSondazna) {
            mjernaPostaja = new RadioSondaznaMjernaPostaja(naziv, mjesto, unesiGeografskuTocku(scanner), unesiSenzore(scanner), visina);
        } else {
            mjernaPostaja = new MjernaPostaja(naziv, mjesto, unesiGeografskuTocku(scanner), unesiSenzore(scanner));
        }
        mjesto.getMjernePostaje().add(mjernaPostaja);
        return mjernaPostaja;
    }

    private static Mjesto unesiMjesto(Scanner scanner) {
        Zupanija zup = unesiZupaniju(scanner);
        System.out.println("Unesite naziv mjesta:");
        String naziv = scanner.nextLine();

        for (int i = 0; i < VrstaMjesta.values().length - 1; i++) {
            System.out.println((i + 1) + ". " + VrstaMjesta.values()[i]);
        }
        System.out.println("Odabir vrste mjesta >> ");
        VrstaMjesta vrstaMjesta = Validator.isVrstaMjesta(scanner);

        Optional<MjernaPostaja> postaja = postaje.getSortedList().stream().filter(p ->
                p.getMjesto().getNaziv().equals(naziv)).findFirst();
        if (postaja.isPresent())
            return postaja.get().getMjesto();

        Mjesto mjesto = new Mjesto(naziv, zup, vrstaMjesta);
        zup.getMjesta().add(mjesto);
        return mjesto;

    }

    private static Zupanija unesiZupaniju(Scanner scanner) {
        Drzava drz = unesiDrzvu(scanner);
        System.out.println("Unesite naziv zupanije:");
        String naziv = scanner.nextLine();

        Optional<MjernaPostaja> postaja = postaje.getSortedList().stream().filter(p ->
                p.getMjesto().getZupanija().getNaziv().equals(naziv)).findFirst();
        if (postaja.isPresent())
            return postaja.get().getMjesto().getZupanija();

        Zupanija zup = new Zupanija(naziv, drz);
        drz.getZupanije().add(zup);
        return zup;
    }

    private static Drzava unesiDrzvu(Scanner scanner) {
        System.out.println("Unesite naziv drzave:");
        String naziv = scanner.nextLine();

        Optional<MjernaPostaja> postaja = postaje.getSortedList().stream().filter(p ->
                p.getMjesto().getZupanija().getDrzava().getNaziv().equals(naziv)).findFirst();

        if (postaja.isPresent()) {
            return postaja.get().getMjesto().getZupanija().getDrzava();
        }

        System.out.println("Unesite povrsinu drzave:");
        BigDecimal povrsina = Validator.isBigDecimal(scanner);
        scanner.nextLine();
        return new Drzava(naziv, povrsina);
    }

    private static GeografskaTocka unesiGeografskuTocku(Scanner scanner) {
        System.out.println("Unesite Geo koordinatu X:");
        BigDecimal geoX = Validator.isBigDecimal(scanner);

        System.out.println("Unesite Geo koordinatu Y:");
        BigDecimal geoY = Validator.isBigDecimal(scanner);
        scanner.nextLine();
        return new GeografskaTocka(geoX, geoY);
    }

    private static List<Senzor> unesiSenzore(Scanner scanner) {
        List<Senzor> senzori = new ArrayList<>();
        senzori.add(unsesiSenzorTemp(scanner));
        senzori.add(unsesiSenzorVlage(scanner));
        senzori.add(unsesiSenzorTlaka(scanner));
        return senzori;
    }

    private static SenzorTemperature unsesiSenzorTemp(Scanner scanner) {
        System.out.println("Unesite elektroničku komponentu za senzor temperature:");
        String naziv = scanner.nextLine();
        System.out.println("Unesite vrijednost senzora temperature:");
        BigDecimal temperatura = Validator.isBigDecimal(scanner);
        scanner.nextLine();
        return new SenzorTemperature(naziv, temperatura, odaberiRadSenzora(scanner));
    }

    private static SenzorVlage unsesiSenzorVlage(Scanner scanner) {
        System.out.println("Unesite vrijednost senzora vlage:");
        BigDecimal vlaga = Validator.isBigDecimal(scanner);
        scanner.nextLine();
        return new SenzorVlage(vlaga, odaberiRadSenzora(scanner));
    }

    private static SenzorTlaka unsesiSenzorTlaka(Scanner scanner) {
        System.out.println("Unesite vrijednost senzora tlaka:");
        BigDecimal tlak = Validator.isBigDecimal(scanner);
        scanner.nextLine();
        return new SenzorTlaka(tlak, odaberiRadSenzora(scanner));
    }

    private static RadSenzora odaberiRadSenzora(Scanner scanner) {
        for (int i = 0; i < RadSenzora.values().length - 1; i++) {
            System.out.println((i + 1) + ". " + RadSenzora.values()[i]);
        }
        System.out.println("Odabir rada senzora >> ");
        while (!scanner.hasNextInt()) {
            LOGGER.error("Neispravan unos rada senzora!");
            System.out.println("Neispravan unos! Odabirite jedan od ponuđenih brojeva");
            System.out.println("Odabir rada senzora >> ");
            scanner.nextLine();
        }

        int rbSenzora = scanner.nextInt();
        if (rbSenzora >= 1 && rbSenzora < VrstaMjesta.values().length) {
            scanner.nextLine();
            return RadSenzora.values()[rbSenzora - 1];
        } else {
            scanner.nextLine();
            return RadSenzora.OSTALO;
        }
    }
}
