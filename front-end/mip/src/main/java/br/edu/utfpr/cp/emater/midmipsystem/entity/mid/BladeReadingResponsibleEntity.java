package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Person;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BladeReadingResponsibleEntity extends Person implements Serializable {

    @ManyToOne
    private City city;

    @Builder
    private static BladeReadingResponsibleEntity create (Long id, String name, City city) {
        BladeReadingResponsibleEntity instance = new BladeReadingResponsibleEntity();
        instance.setId(id);
        instance.setName(name);
        instance.setCity(city);
        
        return instance;
    }
    
    public String getCityName() {
        if (this.getCity() != null)
            return this.getCity().getName();
        
        return null;
    }
    
    public Long getCityId() {
        if (this.getCity() != null)
            return this.getCity().getId();
        
        return null;
    }
    
    public String getIdAsString() {
        if (this.getId() != null)
            return String.valueOf(this.getId());
        
        return null;
    }    
}
