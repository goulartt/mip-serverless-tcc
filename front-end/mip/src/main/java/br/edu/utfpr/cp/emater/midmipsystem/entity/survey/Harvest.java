package br.edu.utfpr.cp.emater.midmipsystem.entity.survey;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.apache.commons.text.WordUtils;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.AuditingPersistenceEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Harvest extends AuditingPersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 50, message = "A identificação da safra deve ter entre 5 e 50 caracteres")
    private String name;

    @EqualsAndHashCode.Include
    @Temporal (TemporalType.DATE)
    private Date begin;

    @EqualsAndHashCode.Include
    @Temporal (TemporalType.DATE)
    private Date end;

    public void setName(String name) {
        this.name = WordUtils.capitalize(name.toLowerCase());
    }
    
    @Builder
    public static Harvest create (Long id, String name, Date begin, Date end) {
        var instance = new Harvest();
        instance.setId(id);
        instance.setName(name);
        instance.setBegin(begin);
        instance.setEnd(end);
        
        return instance;
    }
}
