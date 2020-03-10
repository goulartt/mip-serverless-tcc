package br.edu.utfpr.cp.emater.midmipsystem.view.pulverisation;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.GrowthPhase;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Product;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.PulverisationOperation;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Target;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.UseClass;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.ProductUseClassDifferFromTargetException;
import br.edu.utfpr.cp.emater.midmipsystem.service.pulverisation.PulverisationOperationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component(value = "pulverisationOperationController")
@ViewScoped
@RequiredArgsConstructor
public class PulverisationOperationController extends PulverisationOperation {

    private final PulverisationOperationService pulverisationOperationService;

    @Setter
    @Getter
    private UseClass useClass;

    @Setter
    @Getter
    private Long targetId;

    @Setter
    @Getter
    private List<Target> targetOptions;

    @Setter
    @Getter
    private List<Product> productOptions;

    @Setter
    @Getter
    private Long productId;

    @Setter
    @Getter
    private double productPrice;

    @Setter
    @Getter
    private double productDose;

    public List<Survey> readAllSurveysUniqueEntries() {
        return pulverisationOperationService.readAllSurveysUniqueEntries();
    }

    public String create() {

        var surveyIdAsString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("currentSurveyId");

        Survey currentSurvey = null;

        try {
            currentSurvey = pulverisationOperationService.readSurveyById(Long.parseLong(surveyIdAsString));

        } catch (EntityNotFoundException | NumberFormatException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Operação de pulverização não pode ser concluída porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";
        }

        var newOperation = PulverisationOperation.builder()
                .caldaVolume(this.getCaldaVolume())
                .growthPhase(this.getGrowthPhase())
                .sampleDate(this.getSampleDate())
                .survey(currentSurvey)
                .build();

        newOperation.setOperationOccurrences(this.getOperationOccurrences());

        try {
            pulverisationOperationService.create(newOperation);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Operação de pulverização criada com sucesso!"));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe uma operação de pulverização com essa data para essa UR! Use datas diferentes."));
            return "create.xhtml";

        } catch (EntityNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Operação de pulverização não pode ser feita porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    public String delete(Long anOperationId) {

        try {
            pulverisationOperationService.delete(anOperationId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Operação excluída!"));
            return "index.xhtml";

        } catch (AccessDeniedException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Operação não pode ser excluída porque o usuário não está autorizado!"));
            return "index.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Operação não pode ser excluída porque não foi encontrada na base de dados!"));
            return "index.xhtml";

        } catch (EntityInUseException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Operação não pode ser excluída porque está sendo usada no sistema!"));
            return "index.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    public String selectTargetSurvey(Long id) {

        try {
            var currentSurvey = pulverisationOperationService.readSurveyById(id);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyFieldName", currentSurvey.getFieldName());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyHarvestName", currentSurvey.getHarvestName());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyId", id);

            return "/pulverisation/pulverisation-operation/create-with-survey.xhtml?faces-redirect=true";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Operação de pulverização não pode ser feita porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";
        }

    }

    public GrowthPhase[] readAllGrowthPhases() {
        return GrowthPhase.values();
    }

    public UseClass[] readAllUseClasses() {
        return UseClass.values();
    }

    public void onUseClassChange() {
        if (this.getUseClass() != null) {
            this.setTargetOptions(pulverisationOperationService.readAllTargetsByUseClass(this.getUseClass()));
            this.setProductOptions(pulverisationOperationService.readAllProductByUseClass(this.getUseClass()));
        }
    }

    public void addOccurrence() throws EntityNotFoundException {

        try {
            var product = pulverisationOperationService.readProductById(this.getProductId());
            var target = pulverisationOperationService.readTargetById(this.getTargetId());

            this.addOperationOccurrence(product, this.getProductPrice(), this.getProductDose(), target);

        } catch (ProductUseClassDifferFromTargetException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }
}
