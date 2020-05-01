package br.edu.utfpr.cp.emater.midmipsystem.view.base;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.utfpr.cp.emater.midmipsystem.dto.base.FieldDTO;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Farmer;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.lambda.FieldLambda;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.FieldService;
import br.edu.utfpr.cp.emater.midmipsystem.view.ICRUDController;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// Note that there are issues to resolve regarding the match of supervisors and cities
@Component
@RequestScope
@RequiredArgsConstructor
public class FieldController extends Field implements ICRUDController<Field> {

	private final FieldLambda fieldLambda;
	private final FieldService fieldService;
	
	@Getter
	@Setter
	private List<Long> selectedSupervisorIds;

	@Getter
	@Setter
	private Long selectedCityId;

	@Getter
	@Setter
	private Long selectedFarmerId;

	@Override
	public List<Field> readAll() {
		return fieldLambda.readAll();
	}

	public List<City> readAllCities() {
		return fieldService.readAllCities();
	}

	public List<Farmer> readAllFarmers() {
		return fieldService.readAllFarmers();
	}

	public List<Supervisor> readAllSupervisors() {
		return fieldService.readAllSupervisors();
	}

	@Override
    public String create() {

        try {
        	
        	var currentUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getUser();

			
			var newField = FieldDTO.builder()
        	.name(this.getName())
        	 .location(this.getLocation())
             .cityId(this.getSelectedCityId())
             .farmerId(this.getSelectedFarmerId())
             .supervisors(new HashSet<Long>(this.getSelectedSupervisorIds()))
             .createdBy(currentUser.getId())
             .modifiedBy(currentUser.getId())
             .build();
           

            fieldLambda.create(newField);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Unidade de referência criada com sucesso!"));
            return "index.xhtml";

        } catch (SupervisorNotAllowedInCity ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Um ou mais responsáveis técnicos selecionados não atendem a cidade selecionada para essa UR!"));
            return "create.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe uma unidade de referência com esse nome, nessa cidade para esse produtor!"));
            return "create.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Unidade de referência não pode ser criada porque não foram encontradas as referências para cidade, produtor ou responsável técnico na base de dados!"));
            return "index.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

	@Override
	public String prepareUpdate(Long anId) {

		try {
			Field existentField = fieldLambda.readById(anId);
			this.setId(existentField.getId());
			this.setName(existentField.getName());
			this.setLocation(existentField.getLocation());
			this.setSelectedCityId(existentField.getCityId());
			this.setSelectedFarmerId(existentField.getFarmerId());
			this.setSelectedSupervisorIds(
					existentField.getSupervisors().stream().map(Supervisor::getId).collect(Collectors.toList()));

			return "update.xhtml";

		} catch (EntityNotFoundException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Produtor não pode ser alterado porque não foi encontrado na base de dados!"));
			return "index.xhtml";
		}
	}

	@Override
	public String update() {

		try {
			
			
			var currentUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getUser();
			
			var newField = FieldDTO.builder()
					.id(this.getId())
		        	.name(this.getName())
		        	 .location(this.getLocation())
		             .cityId(this.getSelectedCityId())
		             .farmerId(this.getSelectedFarmerId())
		             .supervisors(new HashSet<Long>(this.getSelectedSupervisorIds()))
		             .createdBy(currentUser.getId())
		             .modifiedBy(currentUser.getId())
		             .build();
		           
			fieldLambda.update(newField);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Unidade de referência alterada"));
			return "index.xhtml";

		} catch (SupervisorNotAllowedInCity ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Um ou mais responsáveis técnicos selecionados não atendem a cidade selecionada para essa UR!"));
			return "update.xhtml";

		} catch (EntityAlreadyExistsException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Já existe uma unidade de referência com esse nome, nessa cidade para esse produtor!"));
			return "update.xhtml";

		} catch (EntityNotFoundException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Unidade de referência não pode ser alterada porque não foi encontrada na base de dados!"));
			return "update.xhtml";

		} catch (AnyPersistenceException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
			return "index.xhtml";
		}

	}

	public String delete(Long anId) {

		try {
			fieldLambda.delete(anId);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Unidade de referência excluída!"));
			return "index.xhtml";

		} catch (AccessDeniedException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"UR não pode ser excluída porque o usuário não está autorizado!"));
			return "index.xhtml";

		} catch (EntityNotFoundException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Unidade de referência não pode ser excluída porque não foi encontrada na base de dados!"));
			return "index.xhtml";

		} catch (EntityInUseException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Unidade de referência não pode ser excluída porque está sendo usado em uma pesquisa!"));
			return "index.xhtml";

		} catch (AnyPersistenceException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
			return "index.xhtml";
		}
	}

}
