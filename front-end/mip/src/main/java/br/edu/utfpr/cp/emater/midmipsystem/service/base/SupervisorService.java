package br.edu.utfpr.cp.emater.midmipsystem.service.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Email;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Region;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.base.SupervisorRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupervisorService implements ICRUDService<Supervisor> {

    private final SupervisorRepository supervisorRepository;
    private final RegionService regionService;

    @Override
    public List<Supervisor> readAll() {
        return List.copyOf(supervisorRepository.findAll());
    }
    
    public List<Region> readAllRegions() {
        return this.regionService.readAll();
    }

    @Override
    public Supervisor readById(Long anId) throws EntityNotFoundException {
        return supervisorRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(Supervisor aSupervisor) throws EntityAlreadyExistsException, AnyPersistenceException {

        if (supervisorRepository.findAll().stream().anyMatch(currentSupervisor -> currentSupervisor.equals(aSupervisor))) {
            throw new EntityAlreadyExistsException();
        }

        try {
            supervisorRepository.save(aSupervisor);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(Supervisor aSupervisor) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException { 
        
        var existentSupervisor = supervisorRepository.findById(aSupervisor.getId()).orElseThrow(EntityNotFoundException::new);
        
        var allSupervisorsWithoutExistentSupervisor = new ArrayList<Supervisor>(supervisorRepository.findAll());
        allSupervisorsWithoutExistentSupervisor.remove(existentSupervisor);

        if (allSupervisorsWithoutExistentSupervisor.stream().anyMatch(currentSupervisor -> currentSupervisor.equals(aSupervisor)))
            throw new EntityAlreadyExistsException();
                
        try {
            existentSupervisor.setName(aSupervisor.getName());
            existentSupervisor.setEmail(aSupervisor.getEmail());
            
            var theRegion = regionService.readById(aSupervisor.getRegionId());
            existentSupervisor.setRegion(theRegion);
            
            supervisorRepository.saveAndFlush(existentSupervisor);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
        
        var existentSupervisor = supervisorRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
        
        try {
            supervisorRepository.delete(existentSupervisor);
            
        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();
            
        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public Region readRegionById(Long selectedRegionId) {
        try {
            return this.regionService.readById(selectedRegionId);
            
        } catch (EntityNotFoundException ex) {
            return Region.builder().build();
        }
    }

    public Optional<Supervisor> readByEmail(@Email String anEmail) {
        
        if (anEmail == null)
            return Optional.empty();
        
        return supervisorRepository.findByEmail(anEmail);
    }
}
