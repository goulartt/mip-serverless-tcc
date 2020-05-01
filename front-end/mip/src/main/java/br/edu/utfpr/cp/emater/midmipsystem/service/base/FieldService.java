package br.edu.utfpr.cp.emater.midmipsystem.service.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Farmer;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FieldService {

	
	private final CityService cityService;
	private final FarmerService farmerService;
	private final SupervisorService supervisorService;


	public List<City> readAllCities() {
		return cityService.readAll();
	}

	public List<Farmer> readAllFarmers() {
		return farmerService.readAll();
	}

	public List<Supervisor> readAllSupervisors() {
		return supervisorService.readAll();
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

}
