package br.edu.utfpr.cp.emater.midmipsystem.view.mid.detail;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.MIDRustSample;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.mid.MIDRustSampleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component(value = "midRustSampleDetailController")
@SessionScope
@RequiredArgsConstructor
public class MIDRustSampleDetailController {

    private final MIDRustSampleService midRustSampleService;

    @Setter
    @Getter
    private Survey currentSurvey;

    public List<MIDRustSample> readAllMIDRustSampleByCurrentSurvey() {
        return midRustSampleService.readAllMIPSampleBySurveyId(this.getCurrentSurvey().getId());
    }

    public String selectTargetSurvey(Long id) {

        try {
            this.setCurrentSurvey(midRustSampleService.readSurveyById(id));

            return "/mid/rust-sample/sample-detail/view-sample-details.xhtml?faces-redirect=true";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Monitoramento da ferrugem não pode ser feito porque a UR não foi encontrada na base de dados!"));
            return "index.xhtml";
        }
    }
}
