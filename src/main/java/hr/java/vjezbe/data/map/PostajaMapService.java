package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.MjestoService;
import hr.java.vjezbe.data.PostajaService;
import hr.java.vjezbe.model.MjernaPostaja;
import hr.java.vjezbe.validator.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class PostajaMapService extends AbstractMapService<MjernaPostaja, Integer> implements PostajaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostajaMapService.class);
    private final MjestoService mjestoService;

    public PostajaMapService(MjestoService mjestoService) {
        this.mjestoService = mjestoService;
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
        return super.save(object);
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
}
