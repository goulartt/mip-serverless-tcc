package br.edu.utfpr.cp.emater.midmipsystem.entity.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.text.WordUtils;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Region extends AuditingPersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Size(min = 5, max = 50, message = "O nome da região deve ter entre 5 e 50 caracteres")
    private String name;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.EAGER)
    private MacroRegion macroRegion;

    @OneToMany
    private Set<City> cities;

    @Builder
    public static Region create(Long id, String name, MacroRegion macroRegion, Set<City> cities) {
        Region result = new Region();
        result.setId(id);
        result.setName(name);
        result.setMacroRegion(macroRegion);
        result.setCities(cities);

        return result;
    }

    public void setName(String name) {
        this.name = WordUtils.capitalize(name.toLowerCase());
    }

    public boolean addCity(City city) {

        if (!isThereACityContainer())
            createCityContainer();

        return this.getCities().add(city);
    }

    public boolean removeCity(City city) {
        if (isThereACityContainer())
            return this.getCities().remove(city);
        
        return false;
    }

    private boolean isThereACityContainer() {
        return !(this.getCities() == null);
    }

    private void createCityContainer() {
        this.setCities(new HashSet<City>());
    }

    public String getMacroRegionName() {
        if (this.getMacroRegion() != null)
            return this.getMacroRegion().getName();
        
        return null;
    }

    public Long getMacroRegionId() {
        if (this.getMacroRegion() != null)
            return this.getMacroRegion().getId();
        
        return null;
    }

    public Set<String> getCityNames() {
        if (this.getCities() != null)
            return this.getCities().stream().map(City::getName).collect(Collectors.toSet());
        
        return null;
    }    
}
