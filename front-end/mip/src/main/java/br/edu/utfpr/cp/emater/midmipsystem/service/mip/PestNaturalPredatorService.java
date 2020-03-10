package br.edu.utfpr.cp.emater.midmipsystem.service.mip;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.PestNaturalPredator;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.mip.PestNaturalPredatorRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PestNaturalPredatorService implements ICRUDService<PestNaturalPredator> {

    private final PestNaturalPredatorRepository pestNaturalPredatorRepository;

    @Override
    public List<PestNaturalPredator> readAll() {
        return List.copyOf(pestNaturalPredatorRepository.findAll());
    }

    @Override
    public PestNaturalPredator readById(Long anId) throws EntityNotFoundException {
        return pestNaturalPredatorRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(PestNaturalPredator aPestNaturalPredator) throws EntityAlreadyExistsException, AnyPersistenceException {

        if (pestNaturalPredatorRepository.findAll().stream().anyMatch(currentPestNaturalPredator -> currentPestNaturalPredator.equals(aPestNaturalPredator))) {
            throw new EntityAlreadyExistsException();
        }

        try {
            pestNaturalPredatorRepository.save(aPestNaturalPredator);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(PestNaturalPredator aPestNaturalPredator) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException { 
        
        var existentPestNaturalPredator = pestNaturalPredatorRepository.findById(aPestNaturalPredator.getId()).orElseThrow(EntityNotFoundException::new);
        
        var allPestNaturalPredatorWithoutExistentPestNaturalPredator = new ArrayList<PestNaturalPredator>(pestNaturalPredatorRepository.findAll());
        allPestNaturalPredatorWithoutExistentPestNaturalPredator.remove(existentPestNaturalPredator);

        if (allPestNaturalPredatorWithoutExistentPestNaturalPredator.stream().anyMatch(currentPestNaturalPredator -> currentPestNaturalPredator.equals(aPestNaturalPredator)))
            throw new EntityAlreadyExistsException();
                
        try {
            existentPestNaturalPredator.setUsualName(aPestNaturalPredator.getUsualName());
                        
            pestNaturalPredatorRepository.saveAndFlush(existentPestNaturalPredator);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
        
        var existentPestNaturalPredator = pestNaturalPredatorRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
        
        try {
            pestNaturalPredatorRepository.delete(existentPestNaturalPredator);
            
        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();
            
        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }
}
