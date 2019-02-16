package hr.java.vjezbe.data.map;

import hr.java.vjezbe.data.PostajaService;
import hr.java.vjezbe.data.SenzorService;
import hr.java.vjezbe.model.Senzor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class SenzorMapService extends AbstractMapService<Senzor, Integer> implements SenzorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostajaMapService.class);
    private final PostajaService postajaService;

    public SenzorMapService(PostajaService postajaService) {
        this.postajaService = postajaService;
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
        return super.save(object);
    }

    @Override
    public void delete(Senzor object) {
        super.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
    }
}
