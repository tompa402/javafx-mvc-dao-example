package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.PostajaService;
import hr.java.vjezbe.data.SenzorService;
import hr.java.vjezbe.model.*;
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

public class SenzorMapService extends AbstractMapService<Senzor, Integer> implements SenzorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostajaMapService.class);
    private static final String SENZORI_PATH = "src/main/resources/dat/senzori.txt";
    private static final String SENZORI_TEMPERATURE_PATH = "src/main/resources/dat/senzori_temperature.txt";
    private static final String SENZORI_VLAGE_PATH = "src/main/resources/dat/senzori_vlage.txt";
    private static final String SENZORI_TLAKA_PATH = "src/main/resources/dat/senzori_tlaka.txt";
    private final PostajaService postajaService;

    public SenzorMapService(PostajaService postajaService) {
        this.postajaService = postajaService;
        loadSenzoriFromFile();
    }

    @Override
    public Set<Senzor> findAll() {
        return super.findAll();
    }

    @Override
    public Senzor findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public Senzor save(Senzor object) {
        postajaService.findById(object.getPostaja().getId()).getSenzori().add(object);
        return spremiSenzor(super.save(object));
    }

    @Override
    public void delete(Senzor object) {
        super.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
    }

    private void loadSenzoriFromFile() {
        List<SenzorTemperature> temperaturni = loadSenzoreTemperatureFromFile();
        List<SenzorTlaka> tlacni = loadSenzoreTlakaFromFile();
        List<SenzorVlage> vlazni = loadSenzoreVlageFromFile();

        final Senzor[] senzor = new Senzor[1];

        try (Stream<String> stream = Files.lines(new File(SENZORI_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaSenzor = 4;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaSenzor) {
                    case 0:
                        Integer id = Integer.parseInt(red);
                        temperaturni.stream().filter(pos -> pos.getId().equals(id)).findFirst().ifPresent(senz ->
                                senzor[0] = senz);
                        tlacni.stream().filter(pos -> pos.getId().equals(id)).findFirst().ifPresent(senz ->
                                senzor[0] = senz);
                        vlazni.stream().filter(pos -> pos.getId().equals(id)).findFirst().ifPresent(senz ->
                                senzor[0] = senz);
                        break;
                    case 1:
                        senzor[0].setVrijednost(new BigDecimal(red));
                        break;
                    case 2:
                        senzor[0].setRadSenzora(RadSenzora.valueOf(red));
                        break;
                    case 3:
                        MjernaPostaja postaja = postajaService.findById(Integer.parseInt(red));
                        senzor[0].setPostaja(postaja);
                        postajaService.findById(senzor[0].getPostaja().getId()).getSenzori().add(senzor[0]);
                        super.save(senzor[0]);
                        break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }

    }

    private List<SenzorTemperature> loadSenzoreTemperatureFromFile() {
        List<SenzorTemperature> lista = new ArrayList<>();
        Integer id = null;
        String naziv;

        try (Stream<String> stream = Files.lines(new File(SENZORI_TEMPERATURE_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaSenzorTemp = 2;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaSenzorTemp) {
                    case 0:
                        id = Integer.parseInt(red);
                        break;
                    case 1: {
                        naziv = red;
                        SenzorTemperature senzor = new SenzorTemperature();
                        senzor.setId(id);
                        senzor.setNaziv(naziv);
                        lista.add(senzor);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
        return lista;
    }

    private List<SenzorVlage> loadSenzoreVlageFromFile() {
        List<SenzorVlage> lista = new ArrayList<>();
        Integer id;

        try (Stream<String> stream = Files.lines(new File(SENZORI_VLAGE_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaSenzorVlage = 1;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaSenzorVlage) {
                    case 0: {
                        id = Integer.parseInt(red);
                        SenzorVlage senzor = new SenzorVlage();
                        senzor.setId(id);
                        lista.add(senzor);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
        return lista;
    }

    private List<SenzorTlaka> loadSenzoreTlakaFromFile() {
        List<SenzorTlaka> lista = new ArrayList<>();
        Integer id;

        try (Stream<String> stream = Files.lines(new File(SENZORI_TLAKA_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaSenzorTlaka = 1;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaSenzorTlaka) {
                    case 0: {
                        id = Integer.parseInt(red);
                        SenzorTlaka senzor = new SenzorTlaka();
                        senzor.setId(id);
                        lista.add(senzor);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
        return lista;
    }

    private Senzor spremiSenzor(Senzor senzor) {
        File mjestaFile = new File(SENZORI_PATH);
        try (FileWriter writer = new FileWriter(mjestaFile, true)) {
            writer.write(senzor.getId() + "\n");
            writer.write(senzor.getVrijednost() + "\n");
            writer.write(senzor.getRadSenzora() + "\n");
            writer.write(senzor.getPostaja().getId() + "\n");

            if (senzor instanceof SenzorTemperature) {
                spremiTemperaturni((SenzorTemperature) senzor);
            } else if (senzor instanceof SenzorVlage) {
                spremiVlazni((SenzorVlage) senzor);
            } else if (senzor instanceof SenzorTlaka) {
                spremiTlacni((SenzorTlaka) senzor);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspješno spremanje senzora temperature!");
            alert.setHeaderText("Uspješno spremanje senzora temperature!");
            alert.setContentText("Uneseni podaci za senzor temperature su uspješno spremljeni.");

            alert.showAndWait();
        } catch (IOException ex) {
            LOGGER.error("Error encountered while writing to file!", ex);
        }
        return senzor;
    }

    private void spremiTemperaturni(SenzorTemperature senzor) throws IOException {
        File mjestaFile = new File(SENZORI_TEMPERATURE_PATH);
        try (FileWriter writer = new FileWriter(mjestaFile, true)) {
            writer.write(senzor.getId() + "\n");
            writer.write(senzor.getNaziv() + "\n");
        }

    }

    private void spremiTlacni(SenzorTlaka senzor) throws IOException {
        File mjestaFile = new File(SENZORI_TLAKA_PATH);
        try (FileWriter writer = new FileWriter(mjestaFile, true)) {
            writer.write(senzor.getId() + "\n");
        }
    }

    private void spremiVlazni(SenzorVlage senzor) throws IOException {
        File mjestaFile = new File(SENZORI_VLAGE_PATH);
        try (FileWriter writer = new FileWriter(mjestaFile, true)) {
            writer.write(senzor.getId() + "\n");
        }
    }
}
