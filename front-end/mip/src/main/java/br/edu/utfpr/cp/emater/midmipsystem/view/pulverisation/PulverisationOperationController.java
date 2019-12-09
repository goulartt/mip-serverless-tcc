package br.edu.utfpr.cp.emater.midmipsystem.view.pulverisation;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.GrowthPhase;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Product;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.PulverisationOperation;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Target;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.TargetCategory;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.pulverisation.PulverisationOperationService;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "pulverisationOperationController")
@ViewScoped
public class PulverisationOperationController extends PulverisationOperation {

    private final PulverisationOperationService pulverisationOperationService;

    @Setter
    @Getter    
    private TargetCategory targetCategory;
    
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
    
    @Autowired
    public PulverisationOperationController(PulverisationOperationService aPulverisationOperationService) {
        this.pulverisationOperationService = aPulverisationOperationService;

    }

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
                            .operationCostCurrency(this.getOperationCostCurrency())
                            .sampleDate(this.getSampleDate())
                            .soyaPrice(this.getSoyaPrice())
                            .survey(currentSurvey)
                            .build();
                
        newOperation.setOperationCostQty(this.getOperationCostQty());
        newOperation.setTotalOperationCostCurrency(this.getOperationCostCurrency());
        newOperation.setTotalOperationCostQty(this.getTotalOperationCostQty());
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
    
    public TargetCategory[] readAllTargetCategories() {
        return TargetCategory.values();
    }
    
    public void onTargetCategoryChange() {
        if (this.getTargetCategory() != null)
            this.setTargetOptions(pulverisationOperationService.readAllTargetsByCategory(this.getTargetCategory()));
    }

    public void onTargetChange() {
        if (this.getTargetId() != null)
            this.setProductOptions(pulverisationOperationService.readAllProductByTarget(this.getTargetId()));
    }
    
    public void addOccurrence() throws EntityNotFoundException {
        var product = pulverisationOperationService.readProductById (this.getProductId());
        var target = pulverisationOperationService.readTargetById (this.getTargetId());
        
        this.addOperationOccurrence(product, this.getProductPrice(), target);
    }
        
}
