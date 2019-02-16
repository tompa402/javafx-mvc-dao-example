package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.MjestoService;
import hr.java.vjezbe.data.ZupanijaService;
import hr.java.vjezbe.javafx.model.Mjesto;
import hr.java.vjezbe.javafx.model.VrstaMjesta;
import hr.java.vjezbe.javafx.model.Zupanija;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MjestoMapService extends AbstractMapService<Mjesto, Integer> implements MjestoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijaMapService.class);
    private static final String MJESTA_PATH = "src/main/resources/dat/mjesta.txt";
    private final ZupanijaService zupanijaService;

    public MjestoMapService(ZupanijaService zupanijaService) {
        this.zupanijaService = zupanijaService;
        loadMjestaFromFile();
    }

    @Override
    public Set<Mjesto> findAll() {
        return super.findAll();
    }

    @Override
    public Mjesto findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public Mjesto save(Mjesto object) {
        zupanijaService.findById(object.getZupanija().getId()).getMjesta().add(object);
        return spremiMjesto(super.save(object));
    }

    @Override
    public void delete(Mjesto object) {
        super.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
    }

    @Override
    public Set<Mjesto> findByName(String name) {
        if (InputValidator.isNullOrEmpty(name))
            return findAll();
        return findAll().stream().filter(o -> o.getNaziv().contains(name)).collect(Collectors.toSet());
    }

    private void loadMjestaFromFile() {
        Integer id = null;
        String naziv = null;
        Zupanija zupanija;
        VrstaMjesta vrstaMjesta = VrstaMjesta.OSTALO;

        try (Stream<String> stream = Files.lines(new File(MJESTA_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaMjesta = 4;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaMjesta) {
                    case 0:
                        id = Integer.parseInt(red);
                        break;
                    case 1:
                        naziv = red;
                        break;
                    case 2:
                        vrstaMjesta = VrstaMjesta.valueOf(red);
                        break;
                    case 3: {
                        zupanija = zupanijaService.findById(Integer.valueOf(red));
                        Mjesto mjesto = new Mjesto(id, naziv, zupanija, vrstaMjesta);
                        zupanijaService.findById(mjesto.getZupanija().getId()).getMjesta().add(mjesto);
                        super.save(mjesto);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
    }

    public Mjesto spremiMjesto(Mjesto mjesto) {
        File mjestaFile = new File(MJESTA_PATH);
        try (FileWriter writer = new FileWriter(mjestaFile, true)) {
            writer.write(mjesto.getId() + "\n");
            writer.write(mjesto.getNaziv() + "\n");
            writer.write(mjesto.getVrstaMjesta().toString() + "\n");
            writer.write(mjesto.getZupanija().getId() + "\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspješno spremanje mjesta!");
            alert.setHeaderText("Uspješno spremanje mjesta!");
            alert.setContentText("Uneseni podaci za mjesto su uspješno spremljeni.");

            alert.showAndWait();
        } catch (IOException ex) {
            LOGGER.error("Error encountered while writing to file!", ex);
        }
        return mjesto;
    }
}
