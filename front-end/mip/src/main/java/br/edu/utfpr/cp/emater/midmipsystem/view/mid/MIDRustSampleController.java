package br.edu.utfpr.cp.emater.midmipsystem.view.mid;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.AsiaticRustTypesLeafInspection;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.AsiaticRustTypesSporeCollector;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.BladeReadingResponsiblePerson;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.MIDRustSample;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.MIDSampleFungicideApplicationOccurrence;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.MIDSampleLeafInspectionOccurrence;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.MIDSampleSporeCollectorOccurrence;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.GrowthPhase;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.mid.MIDRustSampleService;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "midRustSampleController")
@ViewScoped
public class MIDRustSampleController extends MIDRustSample {

    private final MIDRustSampleService midRustSampleService;

    @Setter
    @Getter
    private Long currentSurveyId;

    @Setter
    @Getter
    private String currentSurveyFieldName;

    @Setter
    @Getter
    private String currentSurveyHarvestName;

    @Setter
    @Getter
    private boolean bladeInstalledPreCold;

    @Setter
    @Getter
    private BladeReadingResponsiblePerson bladeResponsiblePerson;

    @Setter
    @Getter
    private Date bladeReadingDate;

    @Setter
    @Getter
    private AsiaticRustTypesSporeCollector bladeReadingRustResultCollector;

    @Setter
    @Getter
    private GrowthPhase growthPhase;

    @Setter
    @Getter
    private AsiaticRustTypesLeafInspection bladeReadingRustResultLeafInspection;

    @Setter
    @Getter
    private boolean asiaticRustApplication;

    @Setter
    @Getter
    private boolean otherDiseasesApplication;

    @Setter
    @Getter
    private Date fungicideApplicationDate;

    @Setter
    @Getter
    private String notes;

    @Autowired
    public MIDRustSampleController(MIDRustSampleService aMIDRustSampleService) {
        this.midRustSampleService = aMIDRustSampleService;

    }

    public List<Survey> readAllSurveysUniqueEntries() {
        return midRustSampleService.readAllSurveysUniqueEntries();
    }

    public List<BladeReadingResponsiblePerson> readAllBladeResponsiblePersons() {
        return midRustSampleService.readAllBladeResponsiblePersons();
    }

    public String create() {

        var surveyIdAsString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("currentSurveyId");

        Survey currentSurvey = null;

        try {
            currentSurvey = midRustSampleService.readSurveyById(Long.parseLong(surveyIdAsString));

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Monitoramento da ferrugem não pode ser feito porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";
        }

        var newSample = MIDRustSample.builder()
                .sampleDate(this.getSampleDate())
                .survey(currentSurvey)
                .build();

        var sporeCollectorOccurrence = MIDSampleSporeCollectorOccurrence.builder()
                .bladeInstalledPreCold(this.isBladeInstalledPreCold())
                .bladeReadingDate(this.getBladeReadingDate())
                .bladeReadingResponsiblePerson(bladeResponsiblePerson)
                .bladeReadingRustResultCollector(this.getBladeReadingRustResultCollector())
                .build();

        var leafInspectionOccurrence = MIDSampleLeafInspectionOccurrence.builder()
                .bladeReadingRustResultLeafInspection(this.getBladeReadingRustResultLeafInspection())
                .growthPhase(this.getGrowthPhase())
                .build();

        var fungicideOccurrence = MIDSampleFungicideApplicationOccurrence.builder()
                .asiaticRustApplication(this.isAsiaticRustApplication())
                .otherDiseasesApplication(this.isOtherDiseasesApplication())
                .fungicideApplicationDate(this.getFungicideApplicationDate())
                .notes(this.getNotes())
                .build();

        newSample.setSporeCollectorOccurrence(sporeCollectorOccurrence);
        newSample.setLeafInspectionOccurrence(leafInspectionOccurrence);
        newSample.setFungicideOccurrence(fungicideOccurrence);

        try {
            midRustSampleService.create(newSample);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Amostra para monitoramento da ferrugem criada com sucesso!"));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe uma amostra com essa data para essa UR! Use datas diferentes."));
            return "create.xhtml";

        } catch (EntityNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Amostra não pode ser feita porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    public String delete(Long aSampleId) {

        try {
            midRustSampleService.delete(aSampleId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Amostra excluída!"));
            return "index.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Amostra não pode ser excluída porque não foi encontrada na base de dados!"));
            return "index.xhtml";

        } catch (EntityInUseException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Amostra não pode ser excluída porque está sendo usada no sistema!"));
            return "index.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    public String selectTargetSurvey(Long id) {

        try {
            var currentSurvey = midRustSampleService.readSurveyById(id);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyFieldName", currentSurvey.getFieldName());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyHarvestName", currentSurvey.getHarvestName());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyId", id);

            return "/mid/rust-sample/create-with-survey.xhtml?faces-redirect=true";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Monitoramento da ferrugem não pode ser feito porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";
        }

    }

    public AsiaticRustTypesSporeCollector[] readAllAsiaticRustTypesSporeCollector() {
        return AsiaticRustTypesSporeCollector.values();
    }

    public GrowthPhase[] readAllGrowthPhases() {
        return GrowthPhase.values();
    }

    public AsiaticRustTypesLeafInspection[] readAllAsiaticRustTypesLeafInspection() {
        return AsiaticRustTypesLeafInspection.values();
    }

}
