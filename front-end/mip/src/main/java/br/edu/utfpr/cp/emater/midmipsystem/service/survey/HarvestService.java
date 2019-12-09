package br.edu.utfpr.cp.emater.midmipsystem.service.survey;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Harvest;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.survey.HarvestRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class HarvestService implements ICRUDService<Harvest> {

    private final HarvestRepository harvestRepository;

    @Autowired
    public HarvestService(HarvestRepository aHarvestRepository) {
        this.harvestRepository = aHarvestRepository;
    }

    public List<Harvest> readAll() {
        return List.copyOf(harvestRepository.findAll());
    }

    public Harvest readById(Long anId) throws EntityNotFoundException {
        return harvestRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(Harvest aHarvest) throws EntityAlreadyExistsException, AnyPersistenceException {

        if (harvestRepository.findAll().stream().anyMatch(currentHarvest -> currentHarvest.equals(aHarvest))) 
            throw new EntityAlreadyExistsException();

        try {
            harvestRepository.save(aHarvest);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(Harvest aHarvest) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        var existentHarvest = harvestRepository.findById(aHarvest.getId()).orElseThrow(EntityNotFoundException::new);
        
        var allHarvests = harvestRepository.findAll();
        var allHarvestButThis = new ArrayList<Harvest>(allHarvests);
        allHarvestButThis.remove(existentHarvest);

        if (allHarvestButThis.stream().anyMatch(currentHarvest -> currentHarvest.equals(aHarvest)))
            throw new EntityAlreadyExistsException();
                
        try {
            existentHarvest.setName(aHarvest.getName());
            existentHarvest.setBegin(aHarvest.getBegin());
            existentHarvest.setEnd(aHarvest.getEnd());
            
            harvestRepository.saveAndFlush(existentHarvest);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
        
        var existentHarvest = harvestRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
        
        try {
            harvestRepository.delete(existentHarvest);
            
        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();
            
        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }
}
