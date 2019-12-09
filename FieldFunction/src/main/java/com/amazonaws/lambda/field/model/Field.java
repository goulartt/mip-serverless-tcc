package com.amazonaws.lambda.field.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.text.WordUtils;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Field  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    private String name;
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    private Farmer farmer;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Supervisor> supervisors;
    
    @Builder
    public static Field create (Long id, String name, String location, City city, Farmer farmer, Set<Supervisor> supervisors) {
        
        Field instance = new Field();
        instance.setId(id);
        instance.setName(name);
        instance.setLocation(location);
        instance.setCity(city);
        instance.setFarmer(farmer);
        instance.setSupervisors(supervisors);
        return instance;
    }

    public boolean addSupervisor(Supervisor aSupervisor) {

        if (noSupervisorContainerCreated())
            createSupervisorContainer();

        return this.getSupervisors().add(aSupervisor);
    }

    public boolean removeSupervisor(Supervisor aSupervisor) {
        if (!noSupervisorContainerCreated())
            return this.getSupervisors().remove(aSupervisor);
        
        return false;
    }

    private boolean noSupervisorContainerCreated() {
        return this.getSupervisors() == null;
    }

    private void createSupervisorContainer() {
        this.setSupervisors(new HashSet<Supervisor>());
    }

    public void setName(String name) {
        this.name = WordUtils.capitalize(name.toLowerCase());
    }

    public String getCityName() {
        return this.getCity().getName();
    }
    
    public Long getCityId(){
        return this.getCity().getId();
    }
    
    public String getStateName() {
        return this.getCity().getState().getName();
    }
    
    public String getFarmerName() {
        return this.getFarmer().getName();
    }
    
    public Long getFarmerId() {
        return this.getFarmer().getId();
    }
    
    public List<String> getSupervisorNames() {
        return this.getSupervisors().stream().map(Supervisor::getName).collect(Collectors.toList());
    }
}
