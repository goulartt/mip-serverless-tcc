package br.edu.utfpr.cp.emater.midmipsystem.service.pulverisation;

import br.edu.utfpr.cp.emater.midmipsystem.service.survey.*;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Product;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.PulverisationOperation;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Target;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.TargetCategory;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.pulverisation.PulverisationOperationRepository;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class PulverisationOperationService {
    
    private final PulverisationOperationRepository pulverisationOperationRepository;
    private final TargetService targetService;
    private final ProductService productService;
    private final SurveyService surveyService;

    @Autowired
    public PulverisationOperationService(
            PulverisationOperationRepository aPulverisationOperationRepository,
            TargetService aTargetService,
            ProductService aProductService,
            SurveyService aSurveyService) {

        this.pulverisationOperationRepository = aPulverisationOperationRepository;
        this.targetService = aTargetService;
        this.productService = aProductService;
        this.surveyService = aSurveyService;
    }


    public List<Survey> readAllSurveysUniqueEntries() {
        return List.copyOf(pulverisationOperationRepository.findAll().stream().map(PulverisationOperation::getSurvey).distinct().collect(Collectors.toList()));
    }

    public void create(PulverisationOperation anOperation) throws EntityAlreadyExistsException, AnyPersistenceException, EntityNotFoundException {

        if (pulverisationOperationRepository.findAll().stream().anyMatch(current -> current.equals(anOperation))) {
            throw new EntityAlreadyExistsException();
        }
        
        anOperation.setDaysAfterEmergence(this.calculateDaysAfterEmergence(anOperation.getSurvey().getEmergenceDate(), anOperation.getSampleDate()));

        try {
            pulverisationOperationRepository.save(anOperation);

        } catch (Exception e) {
            e.printStackTrace();
            throw new AnyPersistenceException();

        }
    }
    
    private int calculateDaysAfterEmergence(Date emergenceDate, Date sampleDate) {

        long diffInMillies = Math.abs(sampleDate.getTime() - emergenceDate.getTime());
        
        var result = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        
        return (int) (result + 1);
    }

    public PulverisationOperation readById(Long anId) throws EntityNotFoundException {
        return pulverisationOperationRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
        
        var existentOperation = pulverisationOperationRepository.findById(anId).orElseThrow(EntityNotFoundException::new);

        try {
            pulverisationOperationRepository.delete(existentOperation);

        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }

    }

    public Survey readSurveyById(Long id) throws EntityNotFoundException {
        return surveyService.readById(id);
    }

    public List<PulverisationOperation> readAllPulverisationOperationBySurveyId(Long aSurveyId) {
        return List.copyOf(pulverisationOperationRepository.findAll().stream().filter(current -> current.getSurvey().getId().equals(aSurveyId)).collect(Collectors.toList()));
    }

    public List<Target> readAllTargetsByCategory(TargetCategory targetCategory) {
        return targetService.readAll().stream().filter(current -> current.getCategory().equals(targetCategory)).collect(Collectors.toList());
    }

    public List<Product> readAllProductByTarget(Long targetId) {
        return productService.readAll().stream().filter(current -> current.getTargetId().equals(targetId)).collect(Collectors.toList());
    }

    public Product readProductById(Long productId) throws EntityNotFoundException {
        return productService.readById(productId);
    }

    public Target readTargetById(Long targetId) throws EntityNotFoundException {
        return targetService.readById(targetId);
    }
}
