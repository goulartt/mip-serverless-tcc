package br.edu.utfpr.cp.emater.midmipsystem.service.mid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.BladeReadingResponsibleEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.mid.BladeReadingResponsibleEntityRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.CityService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BladeReadingResponsibleEntityService implements ICRUDService<BladeReadingResponsibleEntity> {

    private final BladeReadingResponsibleEntityRepository bladeEntityRepository;
    private final CityService cityService;

    @Override
    public List<BladeReadingResponsibleEntity> readAll() {
        return List.copyOf(bladeEntityRepository.findAll());
    }

    @Override
    public BladeReadingResponsibleEntity readById(Long anId) throws EntityNotFoundException {
        return bladeEntityRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(BladeReadingResponsibleEntity aBladeEntity) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        if (bladeEntityRepository.findAll().stream().anyMatch(current -> current.equals(aBladeEntity))) {
            throw new EntityAlreadyExistsException();
        }

        var theCity = cityService.readById(aBladeEntity.getCityId());
        var newEntity = BladeReadingResponsibleEntity.builder().name(aBladeEntity.getName()).city(theCity).build();

        try {

            bladeEntityRepository.save(newEntity);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(BladeReadingResponsibleEntity aBladeEntity) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        var existentBladeEntity = bladeEntityRepository.findById(aBladeEntity.getId()).orElseThrow(EntityNotFoundException::new);

        var allPEntitiesWithoutExistentEntity = new ArrayList<BladeReadingResponsibleEntity>(bladeEntityRepository.findAll());
        allPEntitiesWithoutExistentEntity.remove(existentBladeEntity);

        if (allPEntitiesWithoutExistentEntity.stream().anyMatch(current -> current.equals(aBladeEntity))) {
            throw new EntityAlreadyExistsException();
        }

        var theCity = cityService.readById(aBladeEntity.getCityId());

        try {
            existentBladeEntity.setName(aBladeEntity.getName());
            existentBladeEntity.setCity(theCity);

            bladeEntityRepository.saveAndFlush(existentBladeEntity);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {

        var existentEntity = bladeEntityRepository.findById(anId).orElseThrow(EntityNotFoundException::new);

        var loggedUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        var createdByName = existentEntity.getCreatedBy() != null ? existentEntity.getCreatedBy().getUsername() : "none";

        if (!loggedUser.getUsername().equalsIgnoreCase(createdByName)) {
            throw new AccessDeniedException("Usuário não autorizado para essa exclusão!");
        }

        try {
            bladeEntityRepository.delete(existentEntity);

        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public List<City> readAllCities() {
        return cityService.readAll();
    }

    public City readCityById(Long anId) throws EntityNotFoundException {
        return cityService.readById(anId);
    }

}
