package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.*;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
        return this.getEntity().getName();
    }
    
    public Long getEntityId() {
        return this.getEntity().getId();
    }
    
    public String getEntityCityName() {
        return this.getEntity().getCityName();
    }
    
    public Long getEntityCityId() {
        return this.getEntity().getCityId();
    }
    
}
