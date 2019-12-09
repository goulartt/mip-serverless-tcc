package br.edu.utfpr.cp.emater.midmipsystem.view.survey;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Harvest;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.service.survey.SurveyService;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ViewScoped
public class SurveyController extends Survey {

    private final SurveyService surveyService;
    
    @Getter
    @Setter
    @Size(min = 3, max = 50, message = "A identificação da cultivar deve ter entre 3 e 50 caracteres")
    private String seedName;
    
    @Getter
    @Setter
    private boolean sporeCollectorPresent;

    @Getter
    @Setter
    private Long selectedHarvestId;

    @Getter
    @Setter
    private boolean rustResistant;

    @Getter
    @Setter
    private boolean bt;

    @Getter
    @Setter
    private double totalArea;

    @Getter
    @Setter
    private double totalPlantedArea;

    @Getter
    @Setter
    private double plantPerMeter;

    @Getter
    @Setter
    private String longitude;

    @Getter
    @Setter
    private String latitude;

    @Getter
    @Setter
    private double productivityField;

    @Getter
    @Setter
    private double productivityFarmer;

    @Getter
    @Setter
    private boolean separatedWeight;

    @Getter
    @Setter
    private Date sowedDate;

    @Getter
    @Setter
    private Date emergenceDate;

    @Getter
    @Setter
    private Date harvestDate;

    @Autowired
    public SurveyController(SurveyService aSurveyService) {
        this.surveyService = aSurveyService;
    }

    public List<Survey> readAll() {
        return surveyService.readAll();
    }

    public List<Harvest> readAllHarvests() {
        return surveyService.readAllHarvests();
    }

    public List<Field> readAllFieldsOutOfCurrentSurvey() {
        return surveyService.readAllFields();
    }

    public String create() {

        try {
            var newSurvey = Survey.builder()
                    .bt(this.isBt())
                    .emergenceDate(this.getEmergenceDate())
                    .field(this.getField())
                    .harvest(surveyService.readHarvestById(this.getSelectedHarvestId()))
                    .latitude(this.getLatitude())
                    .longitude(this.getLongitude())
                    .plantPerMeter(this.getPlantPerMeter())
                    .productivityFarmer(this.getProductivityFarmer())
                    .productivityField(this.getProductivityField())
                    .rustResistant(this.isRustResistant())
                    .cultivarName(this.getSeedName())
                    .separatedWeight(this.isSeparatedWeight())
                    .sowedDate(this.getSowedDate())
                    .harvestDate(this.getHarvestDate())
                    .sporeCollectorPresent(this.isSporeCollectorPresent())
                    .totalArea(this.getTotalArea())
                    .totalPlantedArea(this.totalPlantedArea)
                    .build();

            surveyService.create(newSurvey);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", String.format("UR [%s] adicionada na pesquisa da [%s]", newSurvey.getFieldName(), newSurvey.getHarvestName())));

            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A unidade de referência já faz parte dessa pesquisa."));
            return "create.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível adicionar a UR à pesquisa porque a safra ou a UR não foram encontradas na base de dados!"));
            return "index.xhtml";

        } catch (AnyPersistenceException | SupervisorNotAllowedInCity ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";

        }
    }

    public String delete(Long anId) {

        try {
            surveyService.delete(anId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "UR removida da pesquisa!"));
            return "index.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "UR não pode ser removida da pesquisa porque não foi encontrada na base de dados!"));
            return "index.xhtml";

        } catch (EntityInUseException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "UR não pode ser removida da pesquisa porque já existem dados MID/MIP para ela!"));
            return "index.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

}
