package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.DrzavaService;
import hr.java.vjezbe.model.Drzava;
import hr.java.vjezbe.validator.InputValidator;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DrzavaMapService extends AbstractMapService<Drzava, Integer> implements DrzavaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrzavaMapService.class);
    private static final String DRZAVE_PATH = "src/main/resources/dat/drzave.txt";

    public DrzavaMapService() {
        loadDrzaveFromFile();
    }

    @Override
    public Set<Drzava> findAll() {
        return super.findAll();
    }

    @Override
    public Drzava findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public Drzava save(Drzava object) {
        return spremiDrzavu(super.save(object));
    }

    @Override
    public void delete(Drzava object) {
        super.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
    }

    @Override
    public Set<Drzava> findByName(String name) {
        if (InputValidator.isNullOrEmpty(name))
            return findAll();
        return findAll().stream().filter(o -> o.getNaziv().contains(name)).collect(Collectors.toSet());
    }

    private void loadDrzaveFromFile() {
        Integer id = null;
        String naziv = null;
        BigDecimal povrsina;

        try (Stream<String> stream = Files.lines(new File(DRZAVE_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaDrzavu = 3;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaDrzavu) {
                    case 0:
                        id = Integer.parseInt(red);
                        break;
                    case 1:
                        naziv = red;
                        break;
                    case 2: {
                        povrsina = new BigDecimal(red);
                        super.save(new Drzava(id, naziv, povrsina));
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
    }

    private Drzava spremiDrzavu(Drzava drzava) {
        File drzaveFile = new File(DRZAVE_PATH);
        try (FileWriter writer = new FileWriter(drzaveFile, true)) {
            writer.write(drzava.getId() + "\n");
            writer.write(drzava.getNaziv() + "\n");
            writer.write(drzava.getPovrsina() + "\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspješno spremanje države!");
            alert.setHeaderText("Uspješno spremanje države!");
            alert.setContentText("Uneseni podaci za državu su uspješno spremljeni.");

            alert.showAndWait();
        } catch (IOException ex) {
            LOGGER.error("Error encountered while writing to file!", ex);
        }
    return drzava;
    }
}
