package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.MjestoService;
import hr.java.vjezbe.data.PostajaService;
import hr.java.vjezbe.javafx.model.*;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostajaMapService extends AbstractMapService<MjernaPostaja, Integer> implements PostajaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostajaMapService.class);
    private static final String MJERNE_POSTAJE_PATH = "src/main/resources/dat/mjerne_postaje.txt";
    private static final String SONDAZNE_MJERNE_POSTAJE_PATH = "src/main/resources/dat/mjerne_postaje_sondazne.txt";
    private final MjestoService mjestoService;

    public PostajaMapService(MjestoService mjestoService) {
        this.mjestoService = mjestoService;
        loadPostajeFromFile();
    }

    @Override
    public Set<MjernaPostaja> findAll() {
        return super.findAll();
    }

    @Override
    public MjernaPostaja findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public MjernaPostaja save(MjernaPostaja object) {
        mjestoService.findById(object.getMjesto().getId()).getMjernePostaje().add(object);
        return spremiPostaju(super.save(object));
    }

    @Override
    public void delete(MjernaPostaja object) {
        super.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
    }

    @Override
    public Set<MjernaPostaja> findByName(String name) {
        if (InputValidator.isNullOrEmpty(name))
            return findAll();
        return findAll().stream().filter(o -> o.getNaziv().contains(name)).collect(Collectors.toSet());
    }

    private void loadPostajeFromFile() {
        List<RadioSondaznaMjernaPostaja> sondazne = loadSondaznePostajeFromFile();

        final MjernaPostaja[] postaja = new MjernaPostaja[1];

        BigDecimal geoX = null;
        BigDecimal geoY = null;

        try (Stream<String> stream = Files.lines(new File(MJERNE_POSTAJE_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaPostaju = 5;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaPostaju) {
                    case 0:
                        Integer id = Integer.parseInt(red);
                        sondazne.stream().filter(son -> son.getId().equals(id)).findFirst().ifPresentOrElse(son ->
                                postaja[0] = son, () -> {
                            postaja[0] = new MjernaPostaja();
                            postaja[0].setId(id);
                        });
                        break;
                    case 1:
                        postaja[0].setNaziv(red);
                        break;
                    case 2:
                        geoX = new BigDecimal(red);
                        break;
                    case 3:
                        geoY = new BigDecimal(red);
                        break;
                    case 4: {
                        Mjesto mjesto = mjestoService.findById(Integer.valueOf(red));
                        postaja[0].setMjesto(mjesto);
                        postaja[0].setGeografskaTocka(new GeografskaTocka(geoX, geoY));
                        mjestoService.findById(postaja[0].getMjesto().getId()).getMjernePostaje().add(postaja[0]);
                        super.save(postaja[0]);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
    }

    private List<RadioSondaznaMjernaPostaja> loadSondaznePostajeFromFile() {
        List<RadioSondaznaMjernaPostaja> sondazne = new ArrayList<>();
        Integer id = null;
        Integer visina;

        try (Stream<String> stream = Files.lines(new File(SONDAZNE_MJERNE_POSTAJE_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaPostaju = 2;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaPostaju) {
                    case 0:
                        id = Integer.parseInt(red);
                        break;
                    case 1: {
                        visina = Integer.parseInt(red);
                        RadioSondaznaMjernaPostaja sondazna = new RadioSondaznaMjernaPostaja();
                        sondazna.setId(id);
                        sondazna.podesiVisinuPostaje(visina);
                        sondazne.add(sondazna);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
        return sondazne;
    }

    private MjernaPostaja spremiPostaju(MjernaPostaja postaja) {
        File mjestaFile = new File(MJERNE_POSTAJE_PATH);
        try (FileWriter writer = new FileWriter(mjestaFile, true)) {
            writer.write(postaja.getId() + "\n");
            writer.write(postaja.getNaziv() + "\n");
            writer.write(postaja.getGeografskaTocka().getGeoX() + "\n");
            writer.write(postaja.getGeografskaTocka().getGeoY() + "\n");
            writer.write(postaja.getMjesto().getId() + "\n");

            if(postaja instanceof RadioSondazna){
                spremiSondaznu((RadioSondaznaMjernaPostaja)postaja);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspješno spremanje mjerne postaje!");
            alert.setHeaderText("Uspješno spremanje mjerne postaje!");
            alert.setContentText("Uneseni podaci za mjernu postaju su uspješno spremljeni.");

            alert.showAndWait();
        } catch (IOException ex) {
            LOGGER.error("Error encountered while writing to file!", ex);
        }
        return postaja;
    }

    private void spremiSondaznu(RadioSondaznaMjernaPostaja postaja) throws IOException {
        File mjestaFile = new File(SONDAZNE_MJERNE_POSTAJE_PATH);
        try (FileWriter writer = new FileWriter(mjestaFile, true)) {
            writer.write(postaja.getId() + "\n");
            writer.write(postaja.dohvatiVisinuPostaje() + "\n");
        }
    }
}
