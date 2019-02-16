package hr.java.vjezbe.model;

import hr.java.vjezbe.data.*;
import hr.java.vjezbe.data.map.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class Model {
    private static final Logger LOGGER = LoggerFactory.getLogger(Model.class);

    private final DrzavaService drzavaService;
    private final ZupanijaService zupanijaService;
    private final MjestoService mjestoService;
    private final PostajaService postajaService;
    private final SenzorService senzorService;

    public Model() {
        this.drzavaService = new DrzavaMapService();
        this.zupanijaService = new ZupanijaMapService(drzavaService);
        this.mjestoService = new MjestoMapService(zupanijaService);
        this.postajaService = new PostajaMapService(mjestoService);
        this.senzorService = new SenzorMapService(postajaService);
        loadData();
    }

    public DrzavaService getDrzavaService() {
        return drzavaService;
    }

    public ZupanijaService getZupanijaService() {
        return zupanijaService;
    }

    public MjestoService getMjestoService() {
        return mjestoService;
    }

    public PostajaService getPostajaService() {
        return postajaService;
    }

    public SenzorService getSenzorService() {
        return senzorService;
    }

    private void loadData() {
        Drzava drzava = new Drzava("Hrvatska", new BigDecimal("56594"));
        drzavaService.save(drzava);

        Zupanija zupanijaPrva = new Zupanija("Grad Zagreb", drzava);
        zupanijaService.save(zupanijaPrva);
        Zupanija zupanijaDruga = new Zupanija("Ličko-senjska", drzava);
        zupanijaService.save(zupanijaDruga);
        Zupanija zupanijaTreca = new Zupanija("Zadarska", drzava);
        zupanijaService.save(zupanijaTreca);

        Mjesto mjestoPrvo = new Mjesto("Zagreb", zupanijaPrva, VrstaMjesta.OSTALO);
        mjestoService.save(mjestoPrvo);
        Mjesto mjestoDrugo = new Mjesto("Zavižan", zupanijaPrva, VrstaMjesta.SELO);
        mjestoService.save(mjestoDrugo);
        Mjesto mjestoTrece = new Mjesto("Zadar", zupanijaPrva, VrstaMjesta.GRAD);
        mjestoService.save(mjestoTrece);

        MjernaPostaja postajaPrva = new MjernaPostaja("Maksimir", mjestoPrvo,
                new GeografskaTocka(new BigDecimal("5"), new BigDecimal("5")));
        postajaService.save(postajaPrva);
        MjernaPostaja postajaDruga = new MjernaPostaja("Zavižan", mjestoDrugo,
                new GeografskaTocka(new BigDecimal("150"), new BigDecimal("198")));
        postajaService.save(postajaDruga);
        MjernaPostaja postajaTreca = new RadioSondaznaMjernaPostaja("Zavižan", mjestoTrece,
                new GeografskaTocka(new BigDecimal("150"), new BigDecimal("198")), 50);
        postajaService.save(postajaTreca);

        Senzor senzorTempPrvi = new SenzorTemperature("nepoznato", new BigDecimal("25"), RadSenzora.PING, postajaPrva);
        senzorService.save(senzorTempPrvi);
        Senzor senzorTempDrugi = new SenzorTemperature("DTH11", new BigDecimal("18"), RadSenzora.STREAMING, postajaDruga);
        senzorService.save(senzorTempDrugi);
        Senzor senzorTempTreci = new SenzorTemperature("X2X", new BigDecimal("25"), RadSenzora.PING, postajaTreca);
        senzorService.save(senzorTempTreci);

        Senzor senzorTlakPrvi = new SenzorTlaka(new BigDecimal("1025"), RadSenzora.PING, postajaPrva);
        senzorService.save(senzorTlakPrvi);
        Senzor senzorTlakDrugi = new SenzorTlaka(new BigDecimal("999"), RadSenzora.STREAMING, postajaDruga);
        senzorService.save(senzorTlakDrugi);
        Senzor senzorTlakTreci = new SenzorTlaka(new BigDecimal("1000"), RadSenzora.OSTALO, postajaTreca);
        senzorService.save(senzorTlakTreci);

        Senzor senzorVlagaPrvi = new SenzorVlage(new BigDecimal("50"), RadSenzora.OSTALO, postajaPrva);
        senzorService.save(senzorVlagaPrvi);
        Senzor senzorVlagaDrugi = new SenzorVlage(new BigDecimal("70"), RadSenzora.OSTALO, postajaDruga);
        senzorService.save(senzorVlagaDrugi);
        Senzor senzorVlagaTreci = new SenzorVlage(new BigDecimal("42"), RadSenzora.PING, postajaTreca);
        senzorService.save(senzorVlagaTreci);

    }
}
