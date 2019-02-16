package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.DrzavaService;
import hr.java.vjezbe.model.Drzava;
import hr.java.vjezbe.validator.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class DrzavaMapService extends AbstractMapService<Drzava, Integer> implements DrzavaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrzavaMapService.class);

    public DrzavaMapService() {
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
        return super.save(object);
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
}
