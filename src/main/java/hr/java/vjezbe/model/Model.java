package hr.java.vjezbe.model;

import hr.java.vjezbe.data.*;
import hr.java.vjezbe.data.map.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Model {
    private static final Logger LOGGER = LoggerFactory.getLogger(Model.class);

    private final DrzavaService drzavaService;
    private final ZupanijaService zupanijaService;
    private final OpcinaService opcinaService;
    private final MjestoService mjestoService;
    private final PostajaService postajaService;
    private final SenzorService senzorService;

    public Model() {
        this.drzavaService = new DrzavaMapService();
        this.zupanijaService = new ZupanijaMapService(drzavaService);
        this.opcinaService = new OpcinaMapService();
        this.mjestoService = new MjestoMapService(zupanijaService, opcinaService);
        this.postajaService = new PostajaMapService(mjestoService);
        this.senzorService = new SenzorMapService(postajaService);
    }

    public DrzavaService getDrzavaService() {
        return drzavaService;
    }

    public ZupanijaService getZupanijaService() {
        return zupanijaService;
    }

    public OpcinaService getOpcinaService() {
        return opcinaService;
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
}
