package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.DrzavaService;
import hr.java.vjezbe.data.ZupanijaService;
import hr.java.vjezbe.model.Drzava;
import hr.java.vjezbe.model.Zupanija;
import hr.java.vjezbe.validator.InputValidator;
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

public class ZupanijaMapService extends AbstractMapService<Zupanija, Integer> implements ZupanijaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijaMapService.class);
    private static final String ZUPANIJE_PATH = "src/main/resources/dat/zupanije.txt";
    private final DrzavaService drzavaService;

    public ZupanijaMapService(DrzavaService service) {
        this.drzavaService = service;
        loadZupanijeFromFile();
    }

    @Override
    public Set<Zupanija> findAll() {
        return super.findAll();
    }

    @Override
    public Zupanija findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public Zupanija save(Zupanija object) {
        drzavaService.findById(object.getDrzava().getId()).getZupanije().add(object);
        return spremiZupaniju(super.save(object));
    }

    @Override
    public void delete(Zupanija object) {
        super.delete(object);
    }

    @Override
    public Set<Zupanija> findByName(String name) {
        if (InputValidator.isNullOrEmpty(name))
            return findAll();
        return findAll().stream().filter(o -> o.getNaziv().contains(name)).collect(Collectors.toSet());
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
    }

    private void loadZupanijeFromFile() {
        Integer id = null;
        String naziv = "";
        Drzava drzava;

        try (Stream<String> stream = Files.lines(new File(ZUPANIJE_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaZupaniju = 3;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaZupaniju) {
                    case 0:
                        id = Integer.parseInt(red);
                        break;
                    case 1:
                        naziv = red;
                        break;
                    case 2: {
                        drzava = drzavaService.findById(Integer.valueOf(red));
                        Zupanija zup = new Zupanija(id, naziv, drzava);
                        drzavaService.findById(zup.getDrzava().getId()).getZupanije().add(zup);
                        super.save(zup);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
    }

    private Zupanija spremiZupaniju(Zupanija zupanija) {
        File zupanijeFile = new File(ZUPANIJE_PATH);
        try (FileWriter writer = new FileWriter(zupanijeFile, true)) {
            writer.write(zupanija.getId() + "\n");
            writer.write(zupanija.getNaziv() + "\n");
            writer.write(zupanija.getDrzava().getId() + "\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspješno spremanje županije!");
            alert.setHeaderText("Uspješno spremanje županije!");
            alert.setContentText("Uneseni podaci za županiju su uspješno spremljeni.");

            alert.showAndWait();
        } catch (IOException ex) {
            LOGGER.error("Error encountered while writing to file!", ex);
        }
        return zupanija;
    }
}
