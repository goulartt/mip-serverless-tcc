package br.edu.utfpr.cp.emater.midmipsystem.view.mid;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.BladeReadingResponsibleEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mid.BladeReadingResponsiblePerson;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.mid.BladeReadingResponsiblePersonService;
import br.edu.utfpr.cp.emater.midmipsystem.view.ICRUDController;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class BladeReadingResponsiblePersonController extends BladeReadingResponsibleEntity implements ICRUDController<BladeReadingResponsiblePerson> {

    private final BladeReadingResponsiblePersonService bladeReadingPersonService;

    @Getter
    @Setter  
    private Long selectedEntityId;

    @Autowired
    public BladeReadingResponsiblePersonController(BladeReadingResponsiblePersonService aBladeReadingPersonService) {

        this.bladeReadingPersonService = aBladeReadingPersonService;
    }

    @Override
    public List<BladeReadingResponsiblePerson> readAll() {
        return bladeReadingPersonService.readAll();
    }
    
    public List<BladeReadingResponsibleEntity> readAllEntities() {
        return bladeReadingPersonService.readAllEntities();
    }

    @Override
    public String create() {

        try {
            var newPerson = BladeReadingResponsiblePerson.builder()
                    .name(this.getName())
                    .entity(this.bladeReadingPersonService.readEntityById(this.getSelectedEntityId()))
                    .build();

            bladeReadingPersonService.create(newPerson);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", String.format("Profissional [%s] criado com sucesso!", newPerson.getName())));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um profissional com esse nome nessa cidade! Use um nome diferente."));
            return "create.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "O profissional não pode ser criado porque a entidade não foi encontrada na base de dados!"));
            return "create.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String prepareUpdate(Long anId) {

        try {
            var existentPerson = bladeReadingPersonService.readById(anId);
            this.setId(existentPerson.getId());
            this.setName(existentPerson.getName());
            this.setSelectedEntityId(existentPerson.getEntityId());

            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Profissional não pode ser alterado porque não foi encontrado na base de dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String update() {

        try {
            var updatedPerson = BladeReadingResponsiblePerson.builder()
                    .id(this.getId())
                    .name(this.getName())
                    .entity(this.bladeReadingPersonService.readEntityById(this.getSelectedEntityId()))
                    .build();

            bladeReadingPersonService.update(updatedPerson);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Profissional alterado!"));

            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um profissional com esse nome! Use um nome diferente."));
            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Profissional não pode ser alterado porque não foi encontrado na base de dados!"));
            return "update.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }

    }

    public String delete(Long anId) {

        try {
            bladeReadingPersonService.delete(anId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Profissional excluído!"));

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Profissional não pode ser excluído porque não foi encontrado na base de dados!"));

        } catch (EntityInUseException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Profissional não pode ser excluído porque está sendo usado no sistema!"));

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
        }

        return "index.xhtml";
    }

}
