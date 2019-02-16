package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.OpcinaService;
import hr.java.vjezbe.model.Opcina;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpcinaMapService extends AbstractMapService<Opcina, Integer> implements OpcinaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijaMapService.class);
    private static final String OPCINE_PATH = "src/main/resources/dat/opcine.txt";

    public OpcinaMapService() {
        loadOpcineFromFile();
    }

    @Override
    public Set<Opcina> findAll() {
        return super.findAll();
    }

    @Override
    public Opcina findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public Opcina save(Opcina object) {
        return super.save(object);
    }

    @Override
    public void delete(Opcina object) {
        super.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
    }

    private void loadOpcineFromFile() {
        Integer id = null;
        String naziv = null;

        try (Stream<String> stream = Files.lines(new File(OPCINE_PATH).toPath())) {
            List<String> listaStringova = stream.collect(Collectors.toList());

            int brojRedovaZaMjesta = 2;
            for (int i = 0; i < listaStringova.size(); i++) {
                String red = listaStringova.get(i);
                switch (i % brojRedovaZaMjesta) {
                    case 0:
                        id = Integer.parseInt(red);
                        break;
                    case 1: {
                        naziv = red;
                        Opcina opcina = new Opcina(id, naziv);
                        super.save(opcina);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error encountered while reading from file!", ex);
        }
    }
}
