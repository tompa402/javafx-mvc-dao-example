package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.DrzavaService;
import hr.java.vjezbe.data.ZupanijaService;
import hr.java.vjezbe.model.Zupanija;
import hr.java.vjezbe.validator.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class ZupanijaMapService extends AbstractMapService<Zupanija, Integer> implements ZupanijaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijaMapService.class);
    private final DrzavaService drzavaService;

    public ZupanijaMapService(DrzavaService service) {
        this.drzavaService = service;
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
        return super.save(object);
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
}
