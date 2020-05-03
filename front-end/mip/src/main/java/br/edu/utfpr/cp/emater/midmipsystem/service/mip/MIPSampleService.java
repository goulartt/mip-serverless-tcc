package br.edu.utfpr.cp.emater.midmipsystem.service.mip;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.MacroRegion;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Region;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.MIPSample;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.Pest;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.PestDisease;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.PestNaturalPredator;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUser;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.lambda.FieldLambda;
import br.edu.utfpr.cp.emater.midmipsystem.lambda.SurveyLambda;
import br.edu.utfpr.cp.emater.midmipsystem.repository.mip.MIPSampleRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.CityService;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.MacroRegionService;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.RegionService;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.SupervisorService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MIPSampleService {

    private final MIPSampleRepository mipSampleRepository;
    private final SurveyLambda surveyLambda;
    private final PestService pestService;
    private final PestDiseaseService pestDiseaseService;
    private final PestNaturalPredatorService pestNaturalPredatorService;
    private final RegionService regionService;
    private final MacroRegionService macroRegionService;
    private final SupervisorService supervisorService;
    private final CityService cityService;
    private final FieldLambda fieldLambda;

    public List<MIPSample> readAll() {
        return List.copyOf(mipSampleRepository.findAll());
    }

    public List<Survey> readAllSurveysUniqueEntries() {
        return List.copyOf(mipSampleRepository.findAll().stream().map(MIPSample::getSurvey).distinct().collect(Collectors.toList()));
    }

    public void create(MIPSample aSample) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException, AnyPersistenceException, EntityNotFoundException {

        if (mipSampleRepository.findAll().stream().anyMatch(currentSample -> currentSample.equals(aSample))) {
            throw new EntityAlreadyExistsException();
        }

        var theSurvey = surveyLambda.readById(aSample.getSurvey().getId());

        aSample.setSurvey(theSurvey);

        try {
            mipSampleRepository.save(aSample);

        } catch (Exception e) {
            throw new AnyPersistenceException();

        }

    }

    public MIPSample readById(Long anId) throws EntityNotFoundException {
        return mipSampleRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {

        var existentSample = mipSampleRepository.findById(anId).orElseThrow(EntityNotFoundException::new);

        var loggedUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        var createdByName = existentSample.getCreatedBy() != null ? existentSample.getCreatedBy().getUsername() : "none";

        if (!loggedUser.getUsername().equalsIgnoreCase(createdByName)) {
            throw new AccessDeniedException("Usuário não autorizado para essa exclusão!");
        }

        try {
            mipSampleRepository.delete(existentSample);

        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }

    }

    public Survey readSurveyById(Long id) throws EntityNotFoundException {
        return surveyLambda.readById(id);
    }

    public List<Pest> readAllPests() {
        return pestService.readAll();
    }

    public List<PestDisease> readAllPestDiseases() {
        return pestDiseaseService.readAll();
    }

    public List<PestNaturalPredator> readAllPestNaturalPredators() {
        return pestNaturalPredatorService.readAll();
    }

    public List<MIPSample> readAllMIPSampleBySurveyId(Long aSurveyId) {
        return List.copyOf(mipSampleRepository.findAll().stream().filter(sample -> sample.getSurvey().getId().equals(aSurveyId)).collect(Collectors.toList()));
    }
    
    public Optional<Pest> readPestById (Long aPestId) {
        if (aPestId == null)
            return Optional.empty();
        
        try {
            return Optional.of(pestService.readById(aPestId));
            
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }
    
    public Optional<PestNaturalPredator> readPredatorById (Long aPredatorId) {
        if (aPredatorId == null) {
            return Optional.empty();
        }
        
        try {
            return Optional.of(pestNaturalPredatorService.readById(aPredatorId));
            
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }

    public List<Region> readAllRegionsFor(Long aMacroRegionId) {
        return regionService.readAll().stream().filter(currentRegion -> currentRegion.getMacroRegionId().equals(aMacroRegionId)).collect(Collectors.toList());
    }

    public List<MacroRegion> readAllMacroRegions() {
        return macroRegionService.readAll();
    }

    public List<City> readAllCitiesByRegionId(Long aRegionId) throws EntityNotFoundException {
        return new ArrayList<City>(regionService.readById(aRegionId).getCities());
    }

    public List<Field> readAllFieldsByCityId(Long aCityId) {
        return surveyLambda.readAllFields().stream().distinct().filter(field -> field.getCityId().equals(aCityId)).collect(Collectors.toList());
    }

    public List<MacroRegion> readAllMacroRegionsWithSurvey() {
        return this.readAllSurveysUniqueEntries().stream()
                    .map(Survey::getMacroRegion)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .distinct()
                    .collect(Collectors.toList());
    }

    public List<MIPSample> readByMacroRegionId(Long aMacroRegionId) {
        return this.mipSampleRepository.findAll().stream()
                .filter(currentMIPSample -> currentMIPSample.getSurvey() != null)
                .filter(currentMIPSample -> currentMIPSample.getSurvey().getMacroRegion().isPresent())
                .filter(currentSample -> currentSample.getSurvey().getMacroRegion().get().getId().equals(aMacroRegionId))
                .collect(Collectors.toList());
    }

    public List<MIPSample> readByRegionId(Long aRegionId) {
        return this.mipSampleRepository.findAll().stream()
                .filter(currentMIPSample -> currentMIPSample.getSurvey() != null)
                .filter(currentMIPSample -> currentMIPSample.getSurvey().getRegion().isPresent())
                .filter(currentSample -> currentSample.getSurvey().getRegion().get().getId().equals(aRegionId))
                .collect(Collectors.toList());
    }

    public List<MIPSample> readByCityId(Long aCityId) {
        return this.mipSampleRepository.findAll().stream()
                .filter(currentMIPSample -> currentMIPSample.getSurvey() != null)
                .filter(currentMIPSample -> currentMIPSample.getSurvey().getCity().isPresent())
                .filter(currentSample -> currentSample.getSurvey().getCity().get().getId().equals(aCityId))
                .collect(Collectors.toList());
    }

    public List<MIPSample> readByURId(Long anURId) {
        return this.mipSampleRepository.findAll().stream()
                .filter(currentMIPSample -> currentMIPSample.getSurvey() != null)
                .filter(currentMIPSample -> currentMIPSample.getSurvey().getField() != null)
                .filter(currentSample -> currentSample.getSurvey().getField().getId().equals(anURId))
                .collect(Collectors.toList());
    }

    public List<MIPSample> readByMIPUser(MIPUser aMIPUser) {
        
        var supervisorRetrieved = supervisorService.readByEmail(aMIPUser.getEmail());
        
        if (supervisorRetrieved.isEmpty())
            return new ArrayList<MIPSample>();
        
        return this.mipSampleRepository.findAll().stream()
                .filter(currentMIPSample -> currentMIPSample.getSurvey() != null)
                .filter(currentMIPSample -> currentMIPSample.getSurvey().getField() != null)
                .filter(currentMIPSample -> currentMIPSample.getSurvey().getField().getSupervisors() != null)
                .filter(currentMIPSample -> currentMIPSample.getSurvey().getField().getSupervisors().contains(supervisorRetrieved.get()))
                .collect(Collectors.toList());
    }

    public Optional<MacroRegion> readyByMacroRegionId(Long aMacroRegionId) {
        
        if (aMacroRegionId == null)
            return Optional.empty();
        
        try {
            return Optional.of(macroRegionService.readById(aMacroRegionId));
            
        } catch (EntityNotFoundException ex) {
            return Optional.empty();
            
        }
    }

    public Optional<Region> readyRegionById(Long aRegionId) {
        
        if (aRegionId == null)
            return Optional.empty();
        
        try {
            return Optional.of(regionService.readById(aRegionId));
            
        } catch (EntityNotFoundException ex) {
            return Optional.empty();
        }
    }

    public Optional<City> readyCityById(Long aCityId) {
        
        if (aCityId == null)
            return Optional.empty();
        
        try {
            return Optional.of(cityService.readById(aCityId));
            
        } catch (EntityNotFoundException ex) {
            return Optional.empty();
        }
    }

    public Optional<Field> readFieldById(Long aFieldId) {
        
        if (aFieldId == null)
            return Optional.empty();
        
        try {
            return Optional.of(fieldLambda.readById(aFieldId));
            
        } catch (EntityNotFoundException ex) {
            return Optional.empty();
        }
    }
}
