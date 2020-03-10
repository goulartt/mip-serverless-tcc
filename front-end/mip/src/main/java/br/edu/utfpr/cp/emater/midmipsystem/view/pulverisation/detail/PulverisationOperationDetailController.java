package br.edu.utfpr.cp.emater.midmipsystem.view.pulverisation.detail;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.PulverisationOperation;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.pulverisation.PulverisationOperationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component(value = "pulverisationDetailController")
@SessionScope
@RequiredArgsConstructor
public class PulverisationOperationDetailController {

    private final PulverisationOperationService pulverisationService;

    @Setter
    @Getter
    private Survey currentSurvey;

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
    
    public int calculateDaysAfterEmergence(Date sampleDate) {
        
        if (currentSurvey.getEmergenceDate() == null)
            return 0;
        
        long diffInMillies = (sampleDate.getTime() - currentSurvey.getEmergenceDate().getTime());

        if (diffInMillies > 0) {
            var result = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            return (int) (result + 1);
            
        } else
            return 0;
    }
}
