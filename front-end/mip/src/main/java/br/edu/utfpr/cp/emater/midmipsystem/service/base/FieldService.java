package br.edu.utfpr.cp.emater.midmipsystem.service.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.cp.emater.midmipsystem.dto.base.FieldDTO;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Farmer;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.repository.base.FieldRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FieldService implements ICRUDService<Field> {

	@Value("${api.gateway.url}")
	private String ENDPOINT_GATEWAY;

	@Value("${mip.field.find}")
	private String FIELD_GET;
	
	@Value("${mip.field.find-id}")
	private String FIELD_GET_ID;
	
	@Value("${mip.field.create}")
	private String FIELD_CREATE;
	
	@Value("${mip.field.delete}")
	private String FIELD_DELETE;
	
	private final FieldRepository fieldRepository;
	private final CityService cityService;
	private final FarmerService farmerService;
	private final SupervisorService supervisorService;

	public List<Field> readAll() {

		var response = Unirest.get(FIELD_GET).asObject((new GenericType<List<Field>>() {
		})).getBody();

		return response;
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
		try {
			var response = Unirest.get(FIELD_GET_ID)
					.queryString("id", anId)
					.asObject(Field.class).getBody();

			return response;

		} catch (Exception e) {
			throw new EntityNotFoundException();
		}

	}

	private Set<Supervisor> retrieveSupervisors(Set<Supervisor> someSupervisors) throws EntityNotFoundException {
		var result = new HashSet<Supervisor>();

		for (Supervisor currentSupervisor : someSupervisors) {
			result.add(supervisorService.readById(currentSupervisor.getId()));
		}

		return result;
	}

	private boolean isSupervisorIsAllowedInCity(Set<Supervisor> someSupervisors, Long idChosenCity) {

		var idsCitiesSupervisors = someSupervisors.stream().map(Supervisor::getRegion)
				.flatMap(currentRegion -> currentRegion.getCities().stream()).map(City::getId)
				.collect(Collectors.toSet());

		return idsCitiesSupervisors.contains(idChosenCity);
	}

	public void create(FieldDTO newField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			AnyPersistenceException, EntityNotFoundException {

		try {
			var response = Unirest.post(FIELD_CREATE).header("Content-Type", "application/json")
					.body(new ObjectMapper().writeValueAsString(newField)).asJson();
			switch (response.getStatus()) {
			case (201):
				break;
			case (409):
				throw new EntityAlreadyExistsException();
			case (405):
				throw new SupervisorNotAllowedInCity();
			default:
				throw new AnyPersistenceException();
			}
		} catch (JsonProcessingException e) {
			throw new AnyPersistenceException();
		}
	}

	public void update(FieldDTO newField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			EntityNotFoundException, AnyPersistenceException {

		try {

			var response = Unirest.put(FIELD_CREATE).header("Content-Type", "application/json")
					.body(new ObjectMapper().writeValueAsString(newField)).asJson();

			switch (response.getStatus()) {
			case (204):
				break;
			case (405):
				throw new AccessDeniedException("Usuário não autorizado para atualizar essa unidade!");
			case (404):
				throw new EntityNotFoundException();
			default:
				throw new AnyPersistenceException();
			}

		} catch (Exception e) {
			throw new AnyPersistenceException();
		}
	}

	public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {

		var loggedUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser();

		var response = Unirest
				.delete(FIELD_DELETE)
				.queryString("fieldId", anId)
				.queryString("userId", loggedUser.getId())
				.asJson();

		switch (response.getStatus()) {
		case (204):
			break;
		case (405):
			throw new AccessDeniedException("Usuário não autorizado para essa exclusão!");
		case (404):
			throw new EntityNotFoundException();
		default:
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

	public Set<Supervisor> readSupervisorsByIds(List<Long> selectedSupervisorIds) throws EntityNotFoundException {
		var result = new HashSet<Supervisor>();

		for (Long id : selectedSupervisorIds)
			result.add(supervisorService.readById(id));

		return result;
	}

	@Override
	public void create(Field aField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			AnyPersistenceException, EntityNotFoundException {

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
		try {
			var response = Unirest.post(FIELD_CREATE).header("Content-Type", "application/json")
					.body(new ObjectMapper().writeValueAsString(aField)).asJson();
			switch (response.getStatus()) {
			case (201):
				break;
			case (409):
				throw new EntityAlreadyExistsException();
			case (405):
				throw new SupervisorNotAllowedInCity();
			default:
				throw new AnyPersistenceException();
			}
		} catch (JsonProcessingException e) {
			throw new AnyPersistenceException();
		}
	}

	@Override
	public void update(Field entity) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			EntityNotFoundException, AnyPersistenceException {
		// TODO Auto-generated method stub
	}

}
