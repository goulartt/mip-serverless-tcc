package com.amazonaws.lambda.field.model;

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

import org.apache.commons.text.WordUtils;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Region implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
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
        return this.getMacroRegion().getName();
    }

    public Long getMacroRegionId() {
        return this.getMacroRegion().getId();
    }

    public Set<String> getCityNames() {
        return this.getCities().stream().map(City::getName).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return String.format("[Region: name = %s, MacroRegion = %s, Cities = %s]", this.getName(), this.getMacroRegionName(), this.getCityNames().toString());
    }
    
    
}
