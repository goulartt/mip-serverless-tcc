package br.edu.utfpr.cp.emater.midmipsystem.service.survey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.cp.emater.midmipsystem.dto.base.SurveyDTO;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.CropData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Harvest;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.LocationData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.repository.survey.SurveyRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.FieldService;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final HarvestService harvestService;
    private final FieldService fieldService;
    private final CultivarService cultivarService;
	
    @Value("${mip.gateway.url}")
	private String ENDPOINT_GATEWAY;
    
    public List<Survey> readAll() {
    	var response = Unirest.get(ENDPOINT_GATEWAY+"/survey/all").asJson();
    	try {
			List<Survey> values = List.of(new ObjectMapper().readValue(response.getBody().toString(), Survey[].class));
			return values;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

    public Harvest readHarvestById(Long id) throws EntityNotFoundException {
        return harvestService.readById(id);
    }

    public Survey readById(Long anId) throws EntityNotFoundException {
    	try {
			var response = Unirest.get(ENDPOINT_GATEWAY+"/survey")
					.queryString("id", anId)
					.asJson();
			
			if (response.getStatus() != 200)
				throw new EntityNotFoundException();

			return new ObjectMapper().readValue(response.getBody().getObject().toString(), Survey.class);

		} catch (Exception e) {
			throw new EntityNotFoundException();
		}
    }

    public Field readFieldbyId(Long anId) throws EntityNotFoundException {
        return fieldService.readById(anId);
    }

    public List<Survey> readByHarvestId(Long harvestId) throws EntityNotFoundException {
        return List.copyOf(surveyRepository.findAll().stream().filter(currentSurvey -> currentSurvey.getHarvestId().equals(harvestId)).collect(Collectors.toList()));
    }

    public List<Field> readAllFields() {
        return fieldService.readAll();
    }

    public void create(Survey aSurvey) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException, AnyPersistenceException, EntityNotFoundException {

      

    	try {
			var response = Unirest.post(ENDPOINT_GATEWAY+"/survey")
					.header("Content-Type", "application/json")
					.body(new ObjectMapper().writeValueAsString(SurveyDTO.generateFromEntity(aSurvey))).asJson();
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

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
    	
    	var loggedUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser();

		var response = Unirest
				.delete(ENDPOINT_GATEWAY+"/survey")
				.queryString("surveyId", anId)
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

    public List<Harvest> readAllHarvests() {
        return harvestService.readAll();
    }

    public List<Field> readAllFieldsOutOfCurrentHarvest(Long harvestId) {
        var fieldsInCurrentHarvest = this.surveyRepository.findAll().stream()
                .filter(currentSurvey -> currentSurvey.getHarvestId().equals(harvestId))
                .map(Survey::getField)
                .collect(Collectors.toList());

        var allFields = this.fieldService.readAll();

        var allFieldsOutOfCurrentHarvest = new ArrayList<Field>(allFields);

        allFieldsOutOfCurrentHarvest.removeAll(fieldsInCurrentHarvest);

        return allFieldsOutOfCurrentHarvest;
    }

    public void update(Survey updatedSurvey) throws EntityNotFoundException, AnyPersistenceException {

        var currentSurvey = surveyRepository.findById(updatedSurvey.getId()).orElseThrow(EntityNotFoundException::new);

        if (currentSurvey.getCropData() != null) {
            if (updatedSurvey.getEmergenceDate() != null) {
                currentSurvey.getCropData().setEmergenceDate(updatedSurvey.getEmergenceDate());
            }

            if (updatedSurvey.getHarvestDate() != null) {
                currentSurvey.getCropData().setHarvestDate(updatedSurvey.getHarvestDate());
            }

            if (updatedSurvey.getSowedDate() != null) {
                currentSurvey.getCropData().setSowedDate(updatedSurvey.getSowedDate());
            }

        } else {
            currentSurvey.setCropData(
                    CropData.builder()
                            .emergenceDate(updatedSurvey.getEmergenceDate())
                            .harvestDate(updatedSurvey.getHarvestDate())
                            .sowedDate(updatedSurvey.getSowedDate())
                            .build()
            );
        }

        currentSurvey.getProductivityData().setProductivityFarmer(updatedSurvey.getProductivityFarmer());
        currentSurvey.getProductivityData().setProductivityField(updatedSurvey.getProductivityField());
        currentSurvey.getProductivityData().setSeparatedWeight(updatedSurvey.isSeparatedWeight());

        currentSurvey.getPulverisationData().setApplicationCostCurrency(updatedSurvey.getApplicationCostCurrency());
        currentSurvey.getPulverisationData().setSoyaPrice(updatedSurvey.getSoyaPrice());

        if (currentSurvey.getLocationData() != null) {
            if (updatedSurvey.getLatitude() != null) {
                currentSurvey.getLocationData().setLatitude(updatedSurvey.getLatitude());
            }

            if (updatedSurvey.getLongitude() != null) {
                currentSurvey.getLocationData().setLongitude(updatedSurvey.getLongitude());
            }

        } else {
            currentSurvey.setLocationData(
                    LocationData.builder()
                            .latitude(updatedSurvey.getLatitude())
                            .longitude(updatedSurvey.getLongitude())
                            .build()
            );
        }

        currentSurvey.getCultivarData().setBt(updatedSurvey.isBt());
        currentSurvey.getCultivarData().setCultivarName(updatedSurvey.getCultivarName());
        currentSurvey.getCultivarData().setRustResistant(updatedSurvey.isRustResistant());

        currentSurvey.getMidData().setCollectorInstallationDate(updatedSurvey.getCollectorInstallationDate());
        currentSurvey.getMidData().setSporeCollectorPresent(updatedSurvey.isSporeCollectorPresent());

        currentSurvey.getSizeData().setPlantPerMeter(updatedSurvey.getPlantPerMeter());
        currentSurvey.getSizeData().setTotalArea(updatedSurvey.getTotalArea());
        currentSurvey.getSizeData().setTotalPlantedArea(updatedSurvey.getTotalPlantedArea());

        try {
            surveyRepository.saveAndFlush(currentSurvey);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }

    }

    public List<String> searchCultivar(String excerpt) {
        return cultivarService.readByExcerptName(excerpt);
    }
}
