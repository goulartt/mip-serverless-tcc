package br.edu.utfpr.cp.emater.midmipsystem.service.pulverisation;

import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Product;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.PulverisationOperation;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Target;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.UseClass;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.pulverisation.PulverisationOperationRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.survey.SurveyService;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PulverisationOperationService {

    private final PulverisationOperationRepository pulverisationOperationRepository;
    private final TargetService targetService;
    private final ProductService productService;
    private final SurveyService surveyService;

    public List<Survey> readAllSurveysUniqueEntries() {
        return List.copyOf(pulverisationOperationRepository.findAll().stream().map(PulverisationOperation::getSurvey).distinct().collect(Collectors.toList()));
    }

    public void create(PulverisationOperation anOperation) throws EntityAlreadyExistsException, AnyPersistenceException, EntityNotFoundException {

        if (pulverisationOperationRepository.findAll().stream().anyMatch(current -> current.equals(anOperation))) {
            throw new EntityAlreadyExistsException();
        }

        try {
            pulverisationOperationRepository.save(anOperation);

        } catch (Exception e) {
            e.printStackTrace();
            throw new AnyPersistenceException();

        }
    }

    public PulverisationOperation readById(Long anId) throws EntityNotFoundException {
        return pulverisationOperationRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {

        var existentOperation = pulverisationOperationRepository.findById(anId).orElseThrow(EntityNotFoundException::new);

        var loggedUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        var createdByName = existentOperation.getCreatedBy() != null ? existentOperation.getCreatedBy().getUsername() : "none";

        if (!loggedUser.getUsername().equalsIgnoreCase(createdByName)) {
            throw new AccessDeniedException("Usuário não autorizado para essa exclusão!");
        }

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

    public List<Target> readAllTargetsByUseClass(UseClass aUseClass) {
        return targetService.readAll().stream().filter(current -> current.getUseClass().equals(aUseClass)).collect(Collectors.toList());
    }

    public List<Product> readAllProductByUseClass(UseClass aUseClass) {
        return productService.readAll().stream().filter(current -> current.getUseClass().equals(aUseClass)).collect(Collectors.toList());
    }

    public Product readProductById(Long productId) throws EntityNotFoundException {
        return productService.readById(productId);
    }

    public Target readTargetById(Long targetId) throws EntityNotFoundException {
        return targetService.readById(targetId);
    }
}
