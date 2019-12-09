package br.edu.utfpr.cp.emater.midmipsystem.view.mip;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.PestNaturalPredator;
import br.edu.utfpr.cp.emater.midmipsystem.view.ICRUDController;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.mip.PestNaturalPredatorService;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class PestNaturalPredatorController extends PestNaturalPredator implements ICRUDController<PestNaturalPredator> {

    private final PestNaturalPredatorService pestNaturalPredatorService;
    
    @Autowired
    public PestNaturalPredatorController(PestNaturalPredatorService aPestNaturalPredatorService) {
        this.pestNaturalPredatorService = aPestNaturalPredatorService;
    }

    @Override
    public List<PestNaturalPredator> readAll() {
        return pestNaturalPredatorService.readAll();
    }
    
    @Override
    public String create() {
        var newPestNaturalPredator = PestNaturalPredator.builder().usualName(this.getUsualName()).build();

        try {
            pestNaturalPredatorService.create(newPestNaturalPredator);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", String.format("Inimigo natural da praga [%s] criado com sucesso!", this.getUsualName())));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um inimigo natural da praga com esse nome! Use um nome diferente."));
            return "create.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String prepareUpdate(Long anId) {

        try {
            var existentPestNaturalPredator = pestNaturalPredatorService.readById(anId);
            this.setId(existentPestNaturalPredator.getId());
            this.setUsualName(existentPestNaturalPredator.getUsualName());
            
            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Inimigo natural da praga não pode ser alterado porque não foi encontrado na base de dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String update() {
        var updatedPestNaturalPredator = PestNaturalPredator.builder().id(this.getId()).usualName(this.getUsualName()).build();

        try {
            pestNaturalPredatorService.update(updatedPestNaturalPredator);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Inimigo natural da praga alterado"));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um inimigo natural da praga com esse nome! Use um nome diferente."));
            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Inimigo natural da praga não pode ser alterado porque não foi encontrado na base de dados!"));
            return "update.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }

    }

    public String delete(Long anId) {
        
        try {
            pestNaturalPredatorService.delete(anId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Inimigo natural da praga excluído!"));
            return "index.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Inimigo natural da praga não pode ser excluído porque não foi encontrado na base de dados!"));
            return "index.xhtml";
            
        } catch (EntityInUseException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Inimigo natural da praga não pode ser excluído porque está sendo usado em uma amostra!"));
            return "index.xhtml";
            
        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }
}
