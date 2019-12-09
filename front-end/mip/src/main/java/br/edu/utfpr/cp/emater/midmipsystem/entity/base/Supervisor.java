package br.edu.utfpr.cp.emater.midmipsystem.entity.base;

import java.io.Serializable;
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
        return this.getRegion().getName();
    }
    
    public Long getRegionId() {
        return this.getRegion().getId();
    }
    
    public String getMacroRegionName() {
        return this.getRegion().getMacroRegionName();
    }
    
    public Long getMacroRegionId() {
        return this.getRegion().getMacroRegionId();
    }
    
    public String getIdAsString() {
        return String.valueOf(this.getId());
    }
    
    public String getCitiesInRegionNames() {
        return this.getRegion().getCities().toString();
    }
}
