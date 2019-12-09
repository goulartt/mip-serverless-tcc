package br.edu.utfpr.cp.emater.midmipsystem.service.mid;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.BladeReadingResponsibleEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.BladeReadingResponsiblePerson;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.mid.BladeReadingResponsiblePersonRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class BladeReadingResponsiblePersonService implements ICRUDService<BladeReadingResponsiblePerson> {

    private final BladeReadingResponsiblePersonRepository bladePersonRepository;
    private final BladeReadingResponsibleEntityService bladeEntityService;

    @Autowired
    public BladeReadingResponsiblePersonService(BladeReadingResponsiblePersonRepository aBladePersonRepository,
            BladeReadingResponsibleEntityService aBladeEntityService) {

        this.bladePersonRepository = aBladePersonRepository;
        this.bladeEntityService = aBladeEntityService;
    }

    @Override
    public List<BladeReadingResponsiblePerson> readAll() {
        return List.copyOf(bladePersonRepository.findAll());
    }

    @Override
    public BladeReadingResponsiblePerson readById(Long anId) throws EntityNotFoundException {
        return bladePersonRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(BladeReadingResponsiblePerson aBladePerson) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        if (bladePersonRepository.findAll().stream().anyMatch(current -> current.equals(aBladePerson))) {
            throw new EntityAlreadyExistsException();
        }

        var theEntity = bladeEntityService.readById(aBladePerson.getEntityId());
        var newPerson = BladeReadingResponsiblePerson.builder().name(aBladePerson.getName()).entity(theEntity).build();

        try {

            bladePersonRepository.save(newPerson);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(BladeReadingResponsiblePerson aBladePerson) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        var existentBladePerson = bladePersonRepository.findById(aBladePerson.getId()).orElseThrow(EntityNotFoundException::new);

        var allPersonsWithoutExistentPerson = new ArrayList<BladeReadingResponsiblePerson>(bladePersonRepository.findAll());
        allPersonsWithoutExistentPerson.remove(existentBladePerson);

        if (allPersonsWithoutExistentPerson.stream().anyMatch(current -> current.equals(aBladePerson))) {
            throw new EntityAlreadyExistsException();
        }
        
        var theEntity = bladeEntityService.readById(aBladePerson.getEntityId());

        try {
            existentBladePerson.setName(aBladePerson.getName());
            existentBladePerson.setEntity(theEntity);

            bladePersonRepository.saveAndFlush(existentBladePerson);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {

        var existentPerson = bladePersonRepository.findById(anId).orElseThrow(EntityNotFoundException::new);

        try {
            bladePersonRepository.delete(existentPerson);

        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public List<BladeReadingResponsibleEntity> readAllEntities() {
        return bladeEntityService.readAll();
    }

    public BladeReadingResponsibleEntity readEntityById(Long selectedEntityId) throws EntityNotFoundException {
        return bladeEntityService.readById(selectedEntityId);
    }

}
