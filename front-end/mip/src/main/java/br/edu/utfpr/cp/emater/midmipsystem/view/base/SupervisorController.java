package br.edu.utfpr.cp.emater.midmipsystem.view.base;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Region;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.SupervisorService;
import br.edu.utfpr.cp.emater.midmipsystem.view.ICRUDController;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@RequestScope
@RequiredArgsConstructor
public class SupervisorController extends Supervisor implements ICRUDController<Supervisor> {

    private final SupervisorService supervisorService;
    
    @Getter
    @Setter
    private Long selectedRegionId;
    
    @Override
    public List<Supervisor> readAll() {
        return supervisorService.readAll();
    }
    
    public List<Region> readAllRegions() {
        return supervisorService.readAllRegions();
    }    

    @Override
    public String create() {
        var newSupervisor = Supervisor.builder().name(this.getName()).email(this.getEmail()).region(this.supervisorService.readRegionById(this.getSelectedRegionId())).build();

        try {
            supervisorService.create(newSupervisor);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", String.format("Responsável Técnico [%s] criado com sucesso!", this.getName())));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um responsável técnico com esse nome! Use um nome diferente."));
            return "create.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String prepareUpdate(Long anId) {

        try {
            Supervisor existentSupervisor = supervisorService.readById(anId);
            this.setId(existentSupervisor.getId());
            this.setName(existentSupervisor.getName());
            this.setEmail(existentSupervisor.getEmail());
            this.setSelectedRegionId(existentSupervisor.getRegionId());

            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Produtor não pode ser alterado porque não foi encontrado na base de dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String update() {
        var updatedSupervisor = Supervisor.builder().id(this.getId()).name(this.getName()).email(this.getEmail()).region(this.supervisorService.readRegionById(this.getSelectedRegionId())).build();

        try {
            supervisorService.update(updatedSupervisor);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Responsável técnico alterado"));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um responsável técnico com esse nome! Use um nome diferente."));
            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Responsável técnico não pode ser alterado porque não foi encontrado na base de dados!"));
            return "update.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }

    }

    public String delete(Long anId) {
        
        try {
            supervisorService.delete(anId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Responsável técnico excluído!"));
            return "index.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Responsável técnico não pode ser excluído porque não foi encontrado na base de dados!"));
            return "index.xhtml";
            
        } catch (EntityInUseException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Responsável técnico não pode ser excluído porque está sendo usado em uma unidade de referência!"));
            return "index.xhtml";
            
        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

}
