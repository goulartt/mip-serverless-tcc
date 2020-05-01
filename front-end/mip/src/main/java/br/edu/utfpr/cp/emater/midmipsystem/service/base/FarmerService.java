package br.edu.utfpr.cp.emater.midmipsystem.service.base;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Farmer;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.base.FarmerRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FarmerService implements ICRUDService<Farmer> {

    private final FarmerRepository farmerRepository;
    

    @Override
    public List<Farmer> readAll() {
        return List.copyOf(farmerRepository.findAll());
    }

    @Override
    public Farmer readById(Long anId) throws EntityNotFoundException {
        return farmerRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(Farmer aFarmer) throws EntityAlreadyExistsException, AnyPersistenceException {

        if (farmerRepository.findAll().stream().anyMatch(currentFarmer -> currentFarmer.equals(aFarmer))) {
            throw new EntityAlreadyExistsException();
        }

        try {
            farmerRepository.save(aFarmer);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(Farmer aFarmer) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        var existentFarmer = farmerRepository.findById(aFarmer.getId()).orElseThrow(EntityNotFoundException::new);

        if (farmerRepository.findAll().stream().anyMatch(currentFarmer -> currentFarmer.equals(aFarmer)))
            throw new EntityAlreadyExistsException();
                
        try {
            existentFarmer.setName(aFarmer.getName());
            
            farmerRepository.saveAndFlush(existentFarmer);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
        
        var existentFarmer = farmerRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
        
        var loggedUser = ((MIPUserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        var createdByName = existentFarmer.getCreatedBy() != null ? existentFarmer.getCreatedBy().getUsername() : "none";
        
        if (!loggedUser.getUsername().equalsIgnoreCase(createdByName))
            throw new AccessDeniedException("Usuário não autorizado para essa exclusão!");
                
        try {
            farmerRepository.delete(existentFarmer);
            
        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();
            
        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }
}
