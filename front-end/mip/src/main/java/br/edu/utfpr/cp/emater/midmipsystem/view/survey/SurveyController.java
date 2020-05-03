package br.edu.utfpr.cp.emater.midmipsystem.view.survey;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.Size;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Harvest;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.lambda.SurveyLambda;
import br.edu.utfpr.cp.emater.midmipsystem.service.survey.CultivarService;
import br.edu.utfpr.cp.emater.midmipsystem.service.survey.HarvestService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@ViewScoped
@RequiredArgsConstructor
public class SurveyController extends Survey {

	private final HarvestService harvestService;
	private final CultivarService cultivarService;
	private final SurveyLambda surveyLambda;

	@Getter
	@Setter
	@Size(min = 3, max = 50, message = "A identificação da cultivar deve ter entre 3 e 50 caracteres")
	private String cultivarName;

	@Getter
	@Setter
	private boolean sporeCollectorPresent;

	@Getter
	@Setter
	private Date collectorInstallationDate;

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

	@Getter
	@Setter
	private String statusInstallationDatePanel = "hidden-sm hidden-md hidden-lg hidden-xs";

	public List<Survey> readAll() {
		return surveyLambda.readAll();
	}

	public List<Harvest> readAllHarvests() {
		return harvestService.readAll();
	}

	public List<Field> readAllFieldsOutOfCurrentSurvey() {
		return surveyLambda.readAllFields();
	}

	public String create() {

		try {

			var currentUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getUser();

			var newSurvey = Survey.builder().bt(this.isBt()).emergenceDate(this.getEmergenceDate())
					.field(this.getField()).harvest(harvestService.readById(this.getSelectedHarvestId()))
					.latitude(this.getLatitude()).longitude(this.getLongitude()).plantPerMeter(this.getPlantPerMeter())
					.productivityFarmer(this.getProductivityFarmer()).productivityField(this.getProductivityField())
					.rustResistant(this.isRustResistant()).cultivarName(this.getCultivarName())
					.separatedWeight(this.isSeparatedWeight()).sowedDate(this.getSowedDate())
					.harvestDate(this.getHarvestDate()).sporeCollectorPresent(this.isSporeCollectorPresent())
					.collectorInstallationDate(this.getCollectorInstallationDate()).totalArea(this.getTotalArea())
					.totalPlantedArea(this.totalPlantedArea).build();

			newSurvey.setCreatedBy(currentUser);
			newSurvey.setModifiedBy(currentUser);

			surveyLambda.create(newSurvey);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							String.format("UR [%s] adicionada na pesquisa da [%s]", newSurvey.getFieldName(),
									newSurvey.getHarvestName())));

			return "index.xhtml";

		} catch (EntityAlreadyExistsException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"A unidade de referência já faz parte dessa pesquisa."));
			return "create.xhtml";

		} catch (EntityNotFoundException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Não foi possível adicionar a UR à pesquisa porque a safra ou a UR não foram encontradas na base de dados!"));
			return "index.xhtml";

		} catch (AnyPersistenceException | SupervisorNotAllowedInCity ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
			return "index.xhtml";

		}
	}

	public String delete(Long anId) {

		try {
			surveyLambda.delete(anId);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "UR removida da pesquisa!"));
			return "index.xhtml";

		} catch (AccessDeniedException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"UR não pode ser removida da pesquisa porque o usuário não está autorizado!"));
			return "index.xhtml";

		} catch (EntityNotFoundException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"UR não pode ser removida da pesquisa porque não foi encontrada na base de dados!"));
			return "index.xhtml";

		} catch (EntityInUseException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"UR não pode ser removida da pesquisa porque já existem dados MID/MIP para ela!"));
			return "index.xhtml";

		} catch (AnyPersistenceException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
			return "index.xhtml";
		}
	}

	public String prepareUpdate(Long surveyId) {
		try {
			var currentSurvey = surveyLambda.readById(surveyId);

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyFieldName",
					currentSurvey.getFieldName());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyHarvestName",
					currentSurvey.getHarvestName());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSurveyId", surveyId);

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSowedDate",
					currentSurvey.getSowedDate());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentEmergenceDate",
					currentSurvey.getEmergenceDate());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentHarvestDate",
					currentSurvey.getHarvestDate());

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentProductivityField",
					currentSurvey.getProductivityField());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentProductivityFarmer",
					currentSurvey.getProductivityFarmer());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSeparatedWeight",
					currentSurvey.isSeparatedWeight());

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSoyaPrice",
					currentSurvey.getSoyaPrice());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSoyaPrice",
					currentSurvey.getApplicationCostCurrency());

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentLongitude",
					currentSurvey.getLongitude());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentLatitude",
					currentSurvey.getLatitude());

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentCultivarName",
					currentSurvey.getCultivarName());
			this.setCultivarName(currentSurvey.getCultivarName());

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentRestResistant",
					currentSurvey.isRustResistant());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentBT", currentSurvey.isBt());

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentSporeCollectorPresent",
					currentSurvey.isSporeCollectorPresent());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentCollectorInstallationDate",
					currentSurvey.getCollectorInstallationDate());

			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentTotalArea",
					currentSurvey.getTotalArea());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentTotalPlantedArea",
					currentSurvey.getTotalPlantedArea());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currentPlantPerMeter",
					currentSurvey.getPlantPerMeter());

			return "/survey/survey/update.xhtml?faces-redirect=true";

		} catch (EntityNotFoundException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Alteração não pode ser iniciada porque a UR não foi encontrada na base de dados!"));
			return "index.xhtml";
		}
	}

	public void showInstallationDatePanel() {
		if (this.getStatusInstallationDatePanel().equals("hidden-sm hidden-md hidden-lg hidden-xs"))
			this.setStatusInstallationDatePanel("");

		else
			this.setStatusInstallationDatePanel("hidden-sm hidden-md hidden-lg hidden-xs");
	}

	public List<String> searchCultivar(String excerpt) {
		return cultivarService.readByExcerptName(excerpt);
	}

}
