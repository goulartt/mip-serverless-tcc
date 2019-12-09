package br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.AuditingPersistenceEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.apache.commons.text.WordUtils;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class Product extends AuditingPersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @EqualsAndHashCode.Include
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String name;
    
    @Positive (message = "O valor informado deve ser maior que zero")
    private double dose;
    
    @EqualsAndHashCode.Include
    @Enumerated (EnumType.STRING)
    private ProductUnit unit;
    
    @EqualsAndHashCode.Include
    @ManyToOne
    private Target target;

    public void setName(String usualName) {
        this.name = WordUtils.capitalize(usualName.toLowerCase());
    }

    @Builder
    public static Product create(Long id, String name, double dose, ProductUnit unit, Target target) {
        Product instance = new Product();
        instance.setId(id);
        instance.setDose(dose);
        instance.setUnit(unit);
        instance.setName(name);
        instance.setTarget(target);

        return instance;
    }
    
    public String getUnitDescription() {
        return this.getUnit().getDescription();
    }
    
    public String getTargetDescription() {
        return this.getTarget().getDescription();
    }
    
    public Long getTargetId() {
        return this.getTarget().getId();
    }
}
