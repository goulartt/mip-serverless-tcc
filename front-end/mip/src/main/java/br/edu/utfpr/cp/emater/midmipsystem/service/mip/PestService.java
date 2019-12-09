package br.edu.utfpr.cp.emater.midmipsystem.service.mip;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.Pest;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.mip.PestRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class PestService implements ICRUDService<Pest> {

    private final PestRepository pestRepository;

    @Autowired
    public PestService(PestRepository aPestRepository) {
        this.pestRepository = aPestRepository;
    }

    @Override
    public List<Pest> readAll() {
        return List.copyOf(pestRepository.findAll());
    }
    
    @Override
    public Pest readById(Long anId) throws EntityNotFoundException {
        return pestRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(Pest aPest) throws EntityAlreadyExistsException, AnyPersistenceException {

        if (pestRepository.findAll().stream().anyMatch(currentPest -> currentPest.equals(aPest))) {
            throw new EntityAlreadyExistsException();
        }

        try {
            pestRepository.save(aPest);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(Pest aPest) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException { 
        
        var existentPest = pestRepository.findById(aPest.getId()).orElseThrow(EntityNotFoundException::new);
        
        var allPestsWithoutExistentPest = new ArrayList<Pest>(pestRepository.findAll());
        allPestsWithoutExistentPest.remove(existentPest);

        if (allPestsWithoutExistentPest.stream().anyMatch(currentPest -> currentPest.equals(aPest)))
            throw new EntityAlreadyExistsException();
                
        try {
            existentPest.setUsualName(aPest.getUsualName());
            existentPest.setScientificName(aPest.getScientificName());
            existentPest.setPestSize(aPest.getPestSize());
                        
            pestRepository.saveAndFlush(existentPest);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
        
        var existentPest = pestRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
        
        try {
            pestRepository.delete(existentPest);
            
        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();
            
        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

}
