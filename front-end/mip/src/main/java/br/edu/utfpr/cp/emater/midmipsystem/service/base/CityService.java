package br.edu.utfpr.cp.emater.midmipsystem.service.base;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.base.CityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityService {
    
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository aCityRepository) {
        this.cityRepository = aCityRepository;
    }
    
    public List<City> readAll() {
        return List.copyOf(cityRepository.findAll());
    }
    
    public City readById(Long anId) throws EntityNotFoundException {
        return cityRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }
}
