package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Person;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BladeReadingResponsiblePerson extends Person implements Serializable {

    @ManyToOne
    private BladeReadingResponsibleEntity entity;
    
    @Builder
    private static BladeReadingResponsiblePerson create (Long id, String name, BladeReadingResponsibleEntity entity) {
        BladeReadingResponsiblePerson instance = new BladeReadingResponsiblePerson();
        instance.setId(id);
        instance.setName(name);
        instance.setEntity(entity);
        
        return instance;
    }
    
    public String getEntityName() {
        if (this.getEntity() != null)
            return this.getEntity().getName();
        
        return null;
    }
    
    public Long getEntityId() {
        if (this.getEntity() != null)
            return this.getEntity().getId();
        
        return null;
    }
    
    public String getEntityCityName() {
        if (this.getEntity() != null)
            return this.getEntity().getCityName();
        
        return null;
    }
    
    public Long getEntityCityId() {
        if (this.getEntity() != null)
            return this.getEntity().getCityId();
        
        return null;
    }
    
}
