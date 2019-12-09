package br.edu.utfpr.cp.emater.midmipsystem.service.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Farmer;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.repository.base.FieldRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;

@Component
public class FieldService implements ICRUDService<Field> {

    private final FieldRepository fieldRepository;
    private final CityService cityService;
    private final FarmerService farmerService;
    private final SupervisorService supervisorService;
    private final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    @Autowired
    public FieldService(FieldRepository aFieldRepository, CityService aCityService, FarmerService aFarmerService, SupervisorService aSupervisorService) {
        this.fieldRepository = aFieldRepository;
        this.cityService = aCityService;
        this.farmerService = aFarmerService;
        this.supervisorService = aSupervisorService;
    }

    @Override
    public List<Field> readAll() {
        return List.copyOf(fieldRepository.findAll());
    }

    public List<City> readAllCities() {
        return cityService.readAll();
    }

    public List<Farmer> readAllFarmers() {
        return farmerService.readAll();
    }

    public List<Supervisor> readAllSupervisors() {
        return supervisorService.readAll();
    }

    @Override
    public Field readById(Long anId) throws EntityNotFoundException {
        return fieldRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    private Set<Supervisor> retrieveSupervisors(Set<Supervisor> someSupervisors) throws EntityNotFoundException {
        var result = new HashSet<Supervisor>();

        for (Supervisor currentSupervisor : someSupervisors) {
            result.add(supervisorService.readById(currentSupervisor.getId()));
        }

        return result;
    }

    private boolean isSupervisorIsAllowedInCity(Set<Supervisor> someSupervisors, Long idChosenCity) {

        var idsCitiesSupervisors = someSupervisors.stream()
                .map(Supervisor::getRegion)
                .flatMap(currentRegion -> currentRegion.getCities().stream())
                .map(City::getId)
                .collect(Collectors.toSet());

        return idsCitiesSupervisors.contains(idChosenCity);
    }

    public void create(Field aField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException, AnyPersistenceException, EntityNotFoundException {

        /*if (fieldRepository.findAll().stream().anyMatch(currentField -> currentField.equals(aField))) {
            throw new EntityAlreadyExistsException();
        }

        var theCity = cityService.readById(aField.getCityId());
        var someSupervisors = this.retrieveSupervisors(aField.getSupervisors());

        if (!isSupervisorIsAllowedInCity(someSupervisors, theCity.getId())) {
            throw new SupervisorNotAllowedInCity();
        }

        var theFarmer = farmerService.readById(aField.getFarmerId());

        try {
            aField.setCity(theCity);
            aField.setFarmer(theFarmer);
            aField.setSupervisors(someSupervisors);

            fieldRepository.save(aField);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }*/
    	ObjectMapper map = new ObjectMapper();
    	System.out.println("Sending a message to MyQueue.\n");
    	try {
			SendMessageResult sendMessage = sqs.sendMessage(new SendMessageRequest("https://sqs.sa-east-1.amazonaws.com/655636961149/field-sqs", map.writeValueAsString(aField)));
			System.out.println(sendMessage.getMD5OfMessageBody());
    	} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void update(Field aField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        var existentField = fieldRepository.findById(aField.getId()).orElseThrow(EntityNotFoundException::new);

        var allFieldsWithoutExistentField = new ArrayList<Field>(fieldRepository.findAll());
        allFieldsWithoutExistentField.remove(existentField);

        if (allFieldsWithoutExistentField.stream().anyMatch(currentField -> currentField.equals(aField))) {
            throw new EntityAlreadyExistsException();
        }
        
        if (!isSupervisorIsAllowedInCity(aField.getSupervisors(), aField.getCityId())) 
            throw new SupervisorNotAllowedInCity();

        try {
            existentField.setName(aField.getName());
            existentField.setLocation(aField.getLocation());

            var theCity = cityService.readById(aField.getCityId());
            var someSupervisors = this.retrieveSupervisors(aField.getSupervisors());

            var theFarmer = farmerService.readById(aField.getFarmerId());

            existentField.setCity(theCity);
            existentField.setFarmer(theFarmer);
            existentField.setSupervisors(someSupervisors);

            fieldRepository.saveAndFlush(existentField);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {

        var existentField = fieldRepository.findById(anId).orElseThrow(EntityNotFoundException::new);

        try {
            fieldRepository.delete(existentField);

        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public City readCityById(Long selectedCityId) {
        try {
            return this.cityService.readById(selectedCityId);
            
        } catch (EntityNotFoundException ex) {
            return this.cityService.readAll().get(0);
        }
    }

    public Farmer readFarmerById(Long selectedFarmerId) {
        try {
            return this.farmerService.readById(selectedFarmerId);
            
        } catch (EntityNotFoundException ex) {
            return this.farmerService.readAll().get(0);
        }
    }

}
