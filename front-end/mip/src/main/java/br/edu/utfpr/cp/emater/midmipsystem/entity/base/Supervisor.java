package br.edu.utfpr.cp.emater.midmipsystem.entity.base;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Supervisor extends Person implements Serializable {

    @Email (message = "Deve ser informado um e-mail válido")
    @NotNull (message = "Deve ser informado um e-mail válido")
    private String email;

    @ManyToOne
    private Region region;

    @Builder
    private static Supervisor create (Long id, String name, String email, Region region) {
        Supervisor instance = new Supervisor();
        instance.setId(id);
        instance.setName(name);
        instance.setEmail(email);
        instance.setRegion(region);
        
        return instance;
    }
    
    public String getRegionName() {
        if (this.getRegion() != null)
            return this.getRegion().getName();
        
        return null;
    }
    
    public Long getRegionId() {
        if (this.getRegion() != null)
            return this.getRegion().getId();
        
        return null;
    }
    
    public String getMacroRegionName() {
        if (this.getRegion() != null)
            return this.getRegion().getMacroRegionName();
        
        return null;
    }
    
    public Long getMacroRegionId() {
        if (this.getRegion() != null)
            return this.getRegion().getMacroRegionId();
        
        return null;
    }
    
    public String getIdAsString() {
        if (this.getId() != null)
            return String.valueOf(this.getId());
        
        return null;
    }
    
    public String getCitiesInRegionNames() {
        if (this.getRegion() != null)
            return this.getRegion().getCities().toString();
        
        return null;
    }
    
    public Optional<MacroRegion> getMacroRegion() {
        
        if (this.getRegion() == null)
            return Optional.empty();
        
        if (this.getRegion().getMacroRegion() == null)
            return Optional.empty();
        
        return Optional.of(this.getRegion().getMacroRegion());
    }
}
