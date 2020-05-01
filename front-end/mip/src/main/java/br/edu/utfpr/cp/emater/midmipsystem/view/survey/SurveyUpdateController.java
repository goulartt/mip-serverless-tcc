package br.edu.utfpr.cp.emater.midmipsystem.view.survey;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.lambda.SurveyLambda;
import br.edu.utfpr.cp.emater.midmipsystem.service.survey.SurveyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@RequestScope
@RequiredArgsConstructor
@Getter
@Setter
public class SurveyUpdateController {

    private final SurveyService surveyService;
    private final SurveyLambda surveyLambda;


//    ---
    private Long surveyId;

//    ---
    private Date sowedDate;
    private Date emergenceDate;
    private Date harvestDate;

//    ---
    private double productivityField;
    private double productivityFarmer;
    private boolean separatedWeight;

//    ---
    private double soyaPrice;
    private double applicationCostCurrency;

//    ---
    private String longitude;
    private String latitude;

//    ---
    @Size(min = 3, max = 50, message = "A identificação da cultivar deve ter entre 3 e 50 caracteres")
    private String cultivarName;
    private boolean rustResistant;
    private boolean bt;

//    ---
    private boolean sporeCollectorPresent;
    private Date collectorInstallationDate;

//    ---
    private double totalArea;
    private double totalPlantedArea;
    private double plantPerMeter;
    
    public String prepareUpdate(Long surveyId) {

        try {
            var currentSurvey = surveyLambda.readById(surveyId);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyFieldName", currentSurvey.getFieldName());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyHarvestName", currentSurvey.getHarvestName());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyId", surveyId);

            this.setSowedDate(currentSurvey.getSowedDate());
            this.setEmergenceDate(currentSurvey.getEmergenceDate());
            this.setHarvestDate(currentSurvey.getHarvestDate());

            this.setProductivityField(currentSurvey.getProductivityField());
            this.setProductivityFarmer(currentSurvey.getProductivityFarmer());
            this.setSeparatedWeight(currentSurvey.isSeparatedWeight());

            this.setSoyaPrice(currentSurvey.getSoyaPrice());
            this.setApplicationCostCurrency(currentSurvey.getApplicationCostCurrency());

            this.setLongitude(currentSurvey.getLongitude());
            this.setLatitude(currentSurvey.getLatitude());
            
            this.setCultivarName(currentSurvey.getCultivarName());
            this.setRustResistant(currentSurvey.isRustResistant());
            this.setBt(currentSurvey.isBt());

            this.setSporeCollectorPresent(currentSurvey.isSporeCollectorPresent());
            this.setCollectorInstallationDate(currentSurvey.getCollectorInstallationDate());

            this.setTotalArea(currentSurvey.getTotalArea());
            this.setTotalPlantedArea(currentSurvey.getTotalPlantedArea());
            this.setPlantPerMeter(currentSurvey.getPlantPerMeter());

            return "/survey/survey/update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Alteração não pode ser iniciada porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";
        }
    }
    
    public String update() {
        
        var currentSurveyId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("currentSurveyId"));
        
        var updatedSurvey = Survey.builder()
                                        .id(currentSurveyId)
                                        .sowedDate(this.getSowedDate())
                                        .emergenceDate(this.getEmergenceDate())
                                        .harvestDate(this.getHarvestDate())
                                        .productivityField(this.getProductivityField())
                                        .productivityFarmer(this.getProductivityFarmer())
                                        .separatedWeight(this.isSeparatedWeight())
                                        .soyaPrice(this.getSoyaPrice())
                                        .applicationCostCurrency(this.getApplicationCostCurrency())
                                        .longitude(this.getLongitude())
                                        .latitude(this.getLatitude())
                                        .cultivarName(this.getCultivarName())
                                        .rustResistant(this.isRustResistant())
                                        .bt(this.isBt())
                                        .sporeCollectorPresent(this.isSporeCollectorPresent())
                                        .collectorInstallationDate(this.getCollectorInstallationDate())
                                        .totalArea(this.getTotalArea())
                                        .totalPlantedArea(this.getTotalPlantedArea())
                                        .plantPerMeter(this.getPlantPerMeter())
                                .build();

        try {
        	surveyLambda.update(updatedSurvey);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Dados da UR alterados na pesquisa!"));
            return "index.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Dados da UR não puderam ser alterados porque a a pesquisa não foi encontrada na base de dados!"));
            return "update.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }

    }
    
    public List<String> searchCultivar(String excerpt) {
        return surveyService.searchCultivar(excerpt);
    }
}
