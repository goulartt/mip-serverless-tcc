package br.com.utfpr.mip.serverless.entites.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "field")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Field implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    private String name;
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "field_supervisors", joinColumns = @JoinColumn(name = "field_id"), inverseJoinColumns = @JoinColumn(name = "supervisors_id"))
    private Set<Supervisor> supervisors;
    

    @Column(name = "created_at")
    private Long createdAt;
    
    @Column(name = "last_modified")
    private Long lastModified;
    
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private MIPUser createdBy;
    
    @ManyToOne
    @JoinColumn(name = "modified_by_id")
    private MIPUser modifiedBy;


    
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

    
    @JsonIgnore
    public String getCityName() {
        if (this.getCity() != null)
            return this.getCity().getName();
        
        return null;
    }
    @JsonIgnore
    public Long getCityId(){
        if (this.getCity() != null)
            return this.getCity().getId();
        
        return 0L;
    }
    @JsonIgnore
    public String getStateName() {
        if (this.getCity() != null)
            return this.getCity().getState().getName();
        
        return null;
    }
    @JsonIgnore
    public String getFarmerName() {
        if (this.getFarmer()!= null)
            return this.getFarmer().getName();
        
        return null;
    }
    @JsonIgnore
    public Long getFarmerId() {
        if (this.getFarmer()!= null)
            return this.getFarmer().getId();
        
        return 0L;
    }
    @JsonIgnore
    public List<String> getSupervisorNames() {
        if (this.getSupervisors() != null)
            return this.getSupervisors().stream().map(Supervisor::getName).collect(Collectors.toList());
        
        return null;
    }
    @JsonIgnore
    public Optional<MacroRegion> getMacroRegion() {
        
        var supervisorOptional = this.getSupervisors().stream().findAny();
        
        if (supervisorOptional.isPresent())
            return supervisorOptional.get().getMacroRegion();
        
        else
            return Optional.empty();
    }
    @JsonIgnore
    public Optional<Region> getRegion() {
        
        var supervisorOptional = this.getSupervisors().stream().findAny();
        
        if (supervisorOptional.isPresent())
            return Optional.of(supervisorOptional.get().getRegion());
        
        else
            return Optional.empty();
    }
}
