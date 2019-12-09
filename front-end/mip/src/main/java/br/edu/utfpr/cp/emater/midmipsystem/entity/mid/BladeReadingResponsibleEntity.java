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
        return this.getCity().getName();
    }
    
    public Long getCityId() {
        return this.getCity().getId();
    }
    
    public String getIdAsString() {
        return String.valueOf(this.getId());
    }    
}
