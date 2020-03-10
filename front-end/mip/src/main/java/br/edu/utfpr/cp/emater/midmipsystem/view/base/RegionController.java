package br.edu.utfpr.cp.emater.midmipsystem.view.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.MacroRegion;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Region;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.RegionService;
import br.edu.utfpr.cp.emater.midmipsystem.view.ICRUDController;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// Note that there are issues to resolve when updating a Region
@Component
@RequestScope
@RequiredArgsConstructor
public class RegionController extends Region implements ICRUDController<Region> {

    private final RegionService regionService;

    @Getter
    @Setter
    private List<City> selectedCities;

    @Getter
    @Setter
    private Long selectedMacroRegion;

    @Override
    public List<Region> readAll() {
        return regionService.readAll();
    }
    
    public List<MacroRegion> readAllMacroRegions() {
        return regionService.readAllMacroRegions();
    }

    public List<City> readAllCitiesWithoutRegion() {
        return regionService.readAllCitiesWithoutRegion();
    }

    public List<City> readAllCitiesForUpdate() {

        var allCitiesWithoutRegion = this.readAllCitiesWithoutRegion();

        if (this.getCities() != null) {

            var citiesInThisRegion = this.getCities();
            var allCities = new ArrayList<City>(allCitiesWithoutRegion);
            allCities.addAll(citiesInThisRegion);

            return allCities;

        } else {
            return allCitiesWithoutRegion;
        }

    }

    @Override
    public String create() {

        try {
            var newRegion = Region.builder()
                    .name(this.getName())
                    .macroRegion(this.regionService.readMacroRegionById(this.getSelectedMacroRegion()))
                    .cities(new HashSet<City>(this.getSelectedCities()))
                    .build();

            regionService.create(newRegion);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", String.format("Região [%s] criada com sucesso!", newRegion.getName())));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe uma região com esse nome nessa macrorregião! Use um nome diferente, ou selecione outra macrorregião."));
            return "create.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A região não pode ser criada porque a cidade ou a macrorregião não foram encontradas na base de dados!"));
            return "create.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String prepareUpdate(Long anId) {

        try {
            Region existentRegion = regionService.readById(anId);
            this.setId(existentRegion.getId());
            this.setName(existentRegion.getName());
            this.setSelectedMacroRegion(existentRegion.getMacroRegion().getId());
            this.setCities(existentRegion.getCities());

            this.setSelectedCities(new ArrayList<City>(existentRegion.getCities()));

            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Região não pode ser alterada porque não foi encontrada na base de dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String update() {

        try {
            var updatedRegion = Region.builder()
                    .id(this.getId())
                    .name(this.getName())
                    .macroRegion(this.regionService.readMacroRegionById(this.getSelectedMacroRegion()))
                    .cities(new HashSet<City>(this.getSelectedCities()))
                    .build();

            regionService.update(updatedRegion);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Região alterada!"));

            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe uma região com esse nome! Use um nome diferente."));
            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Região não pode ser alterada porque não foi encontrada na base de dados!"));
            return "update.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }

    }

    public String delete(Long anId) {

        try {
            regionService.delete(anId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Região excluída!"));

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Região não pode ser excluída porque não foi encontrada na base de dados!"));

        } catch (EntityInUseException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Região não pode ser excluída porque está sendo usada no sistema!"));

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
        }

        return "index.xhtml";
    }

}
