package br.edu.utfpr.cp.emater.midmipsystem.view.pulverisation.detail;

import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.PulverisationOperation;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.pulverisation.PulverisationOperationService;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component(value = "pulverisationDetailController")
@SessionScope
public class PulverisationOperationDetailController {

    private final PulverisationOperationService pulverisationService;

    @Setter
    @Getter
    private Survey currentSurvey;

    @Autowired
    public PulverisationOperationDetailController(PulverisationOperationService aPulverisationService) {
        this.pulverisationService = aPulverisationService;
    }

    public List<PulverisationOperation> readAllPulverisationOperationBySurvey() {
        return pulverisationService.readAllPulverisationOperationBySurveyId(this.getCurrentSurvey().getId());
    }

    public String selectTargetSurvey(Long id) {

        try {
            this.setCurrentSurvey(pulverisationService.readSurveyById(id));

            return "/pulverisation/pulverisation-operation/sample-detail/view-sample-details.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Operação de pulverização não pode ser feita porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";
        }
    }
}
