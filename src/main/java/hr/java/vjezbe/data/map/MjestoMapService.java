package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.MjestoService;
import hr.java.vjezbe.data.ZupanijaService;
import hr.java.vjezbe.model.Mjesto;
import hr.java.vjezbe.validator.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class MjestoMapService extends AbstractMapService<Mjesto, Integer> implements MjestoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijaMapService.class);
    private final ZupanijaService zupanijaService;

    public MjestoMapService(ZupanijaService zupanijaService) {
        this.zupanijaService = zupanijaService;
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
        return super.save(object);
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
}
