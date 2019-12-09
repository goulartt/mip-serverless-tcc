package br.edu.utfpr.cp.emater.midmipsystem.service.survey;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.*;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Harvest;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.repository.survey.SurveyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final HarvestService harvestService;
    private final FieldService fieldService;

    @Autowired
    public SurveyService(SurveyRepository aSurveyRepository, HarvestService aHarvestService, FieldService aFieldService) {
        this.surveyRepository = aSurveyRepository;
        this.harvestService = aHarvestService;
        this.fieldService = aFieldService;
    }

    public List<Survey> readAll() {
        return List.copyOf(surveyRepository.findAll());
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
        
        var existentHarvest = surveyRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
        
        try {
            surveyRepository.delete(existentHarvest);
            
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
}
