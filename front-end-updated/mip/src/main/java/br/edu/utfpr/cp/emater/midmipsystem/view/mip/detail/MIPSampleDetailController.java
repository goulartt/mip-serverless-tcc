package br.edu.utfpr.cp.emater.midmipsystem.view.mip.detail;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.MIPSample;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.mip.MIPSampleService;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component(value = "mipSampleDetailController")
@SessionScope
@RequiredArgsConstructor
public class MIPSampleDetailController {

    private final MIPSampleService mipSampleService;

    @Setter
    @Getter
    private Survey currentSurvey;

    public List<MIPSample> readAllMIPSampleBySurvey() {
        return mipSampleService.readAllMIPSampleBySurveyId(this.getCurrentSurvey().getId());
    }

    public String selectTargetSurvey(Long id) {

        try {
            this.setCurrentSurvey(mipSampleService.readSurveyById(id));

            return "/mip/mip-sample/sample-detail/view-sample-details.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Anotação de campo não pode ser feita porque a UR não foi encontrada na base de dados!"));
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
