package br.edu.utfpr.cp.emater.midmipsystem.service.pulverisation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Target;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.pulverisation.TargetRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TargetService implements ICRUDService<Target> {

    private final TargetRepository targetRepository;
    
    @Override
    public List<Target> readAll() {
        return List.copyOf(targetRepository.findAll());
    }

    public Target readById(Long anId) throws EntityNotFoundException {
        return targetRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(Target aTarget) throws EntityAlreadyExistsException, AnyPersistenceException, EntityNotFoundException {

        if (targetRepository.findAll().stream().anyMatch(currentTarget -> currentTarget.equals(aTarget))) {
            throw new EntityAlreadyExistsException();
        }

        try {
            targetRepository.save(aTarget);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(Target aTarget) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        var existentTarget = targetRepository.findById(aTarget.getId()).orElseThrow(EntityNotFoundException::new);
        
        var allTargetsWithoutExistentTarget = new ArrayList<Target>(targetRepository.findAll());
        allTargetsWithoutExistentTarget.remove(existentTarget);
        
        if (allTargetsWithoutExistentTarget.stream().anyMatch(currentTarget -> currentTarget.equals(aTarget))) {
            throw new EntityAlreadyExistsException();
        }

        try {
            existentTarget.setDescription(aTarget.getDescription());
            existentTarget.setUseClass(aTarget.getUseClass());

            targetRepository.saveAndFlush(existentTarget);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, AnyPersistenceException, EntityInUseException {
        
        var existentTarget = targetRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
        
        try {
            targetRepository.delete(existentTarget);
            
        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();
            
        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }
}
