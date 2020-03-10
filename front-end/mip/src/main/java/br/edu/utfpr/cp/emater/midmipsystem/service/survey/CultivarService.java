package br.edu.utfpr.cp.emater.midmipsystem.service.survey;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Cultivar;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.survey.CultivarRepository;
import br.edu.utfpr.cp.emater.midmipsystem.repository.survey.SurveyRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CultivarService implements ICRUDService<Cultivar> {

    private final CultivarRepository cultivarRepository;
//    Notice this strategy does not comply with the architectural style used 
//    throughout this app. We should be using the surveyService instead. 
//    However, there is a cyclical dependency here that needs to be fixed.
    private final SurveyRepository surveyRepository;
    
    public List<String> readByExcerptName(String excerpt) {
        return List.copyOf(cultivarRepository.findByNameContainingIgnoreCase(excerpt).stream().map(Cultivar::getName).collect(Collectors.toList()));
    }

    public List<Cultivar> readAll() {
        return List.copyOf(cultivarRepository.findAll());
    }

    public Cultivar readById(Long anId) throws EntityNotFoundException {
        return cultivarRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(Cultivar aCultivar) throws EntityAlreadyExistsException, AnyPersistenceException {

        if (cultivarRepository.findAll().stream().map(Cultivar::getName).anyMatch(currentName -> currentName.equalsIgnoreCase(aCultivar.getName()))) 
            throw new EntityAlreadyExistsException();

        try {
            cultivarRepository.save(aCultivar);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(Cultivar cultivar) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        var existentCultivar = cultivarRepository.findById(cultivar.getId()).orElseThrow(EntityNotFoundException::new);
        
        var allCultivares = cultivarRepository.findAll();
        var allCultivaresButThis = new ArrayList<Cultivar>(allCultivares);
        allCultivaresButThis.remove(existentCultivar);

        if (allCultivaresButThis.stream().map(Cultivar::getName).anyMatch(currentName-> currentName.equalsIgnoreCase(cultivar.getName())))
            throw new EntityAlreadyExistsException();
                
        try {
            existentCultivar.setName(cultivar.getName());
            
            cultivarRepository.saveAndFlush(existentCultivar);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
        
        var existentCultivar = cultivarRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
        
        try {
            if (surveyRepository
                    .findAll()
                    .stream()
                    .map(currentSurvey -> currentSurvey.getCultivarData().getCultivarName())
                    .anyMatch(name -> name.equalsIgnoreCase(existentCultivar.getName()))
                ) 
                    throw new DataIntegrityViolationException("Entity in use!");
            
            
            cultivarRepository.delete(existentCultivar);
            
        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();
            
        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }
}
