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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        return surveyRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
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

        if (surveyRepository.findAll().stream().anyMatch(currentSurvey -> currentSurvey.equals(aSurvey))) {
            throw new EntityAlreadyExistsException();
        }

        var theField = fieldService.readById(aSurvey.getFieldId());
        var theHarvest = harvestService.readById(aSurvey.getHarvestId());

        aSurvey.setField(theField);
        aSurvey.setHarvest(theHarvest);

        try {
            surveyRepository.save(aSurvey);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {

        var existentSurvey = surveyRepository.findById(anId).orElseThrow(EntityNotFoundException::new);

        var loggedUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        var createdByName = existentSurvey.getCreatedBy() != null ? existentSurvey.getCreatedBy().getUsername() : "none";

        if (!loggedUser.getUsername().equalsIgnoreCase(createdByName)) {
            throw new AccessDeniedException("Usuário não autorizado para essa exclusão!");
        }
        
        try {
            surveyRepository.delete(existentSurvey);

        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();

        } catch (Exception e) {
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
