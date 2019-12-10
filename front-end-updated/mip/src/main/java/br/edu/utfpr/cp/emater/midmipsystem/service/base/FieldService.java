package br.edu.utfpr.cp.emater.midmipsystem.service.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.dto.ResponseDTO;
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
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FieldService implements ICRUDService<Field> {

	private static final String ENDPOINT_GATEWAY = "https://oyl9jbjobd.execute-api.sa-east-1.amazonaws.com/mip-gateway/field";

	private final FieldRepository fieldRepository;
	private final CityService cityService;
	private final FarmerService farmerService;
	private final SupervisorService supervisorService;

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

		var idsCitiesSupervisors = someSupervisors.stream().map(Supervisor::getRegion)
				.flatMap(currentRegion -> currentRegion.getCities().stream()).map(City::getId)
				.collect(Collectors.toSet());

		return idsCitiesSupervisors.contains(idChosenCity);
	}

	public void create(FieldDTO newField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			AnyPersistenceException, EntityNotFoundException {

		try {

			HttpResponse<ResponseDTO> response = Unirest.post(ENDPOINT_GATEWAY).header("Content-Type", "application/json")
					.body(FieldDTO.generateJSON(newField)).asObject(ResponseDTO.class);
			
			switch (response.getBody().getStatusCode()) {
			case (200):
				break;
			case (409):
				throw new EntityAlreadyExistsException();
			case (405):
				throw new SupervisorNotAllowedInCity();
			default:
				throw new AnyPersistenceException();
			}

		} catch (Exception e) {
			throw new AnyPersistenceException();
		}
	}

	@Override
	public void update(Field aField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			EntityNotFoundException, AnyPersistenceException {

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

		var loggedUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser();
		var createdByName = existentField.getCreatedBy() != null ? existentField.getCreatedBy().getUsername() : "none";

		if (!loggedUser.getUsername().equalsIgnoreCase(createdByName))
			throw new AccessDeniedException("Usuário não autorizado para essa exclusão!");

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

	public Set<Supervisor> readSupervisorsByIds(List<Long> selectedSupervisorIds) throws EntityNotFoundException {
		var result = new HashSet<Supervisor>();

		for (Long id : selectedSupervisorIds)
			result.add(supervisorService.readById(id));

		return result;
	}

	@Override
	public void create(Field entity) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			AnyPersistenceException, EntityNotFoundException {
		// TODO Auto-generated method stub
		
	}

}
