package br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class Product extends AuditingPersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @EqualsAndHashCode.Include
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String name;
    
    @EqualsAndHashCode.Include
    @Enumerated (EnumType.STRING)
    private ProductUnit unit;
    
    @EqualsAndHashCode.Include
    @Enumerated (EnumType.STRING)
    private UseClass useClass;
    
    private String concentrationActiveIngredient;
    private Long registerNumber;
    private String company;
    private String activeIngredient;
    
    @Enumerated (EnumType.STRING)
    private ToxiClass toxiClass;

    public void setName(String usualName) {
        this.name = WordUtils.capitalize(usualName.toLowerCase());
    }

    @Builder
    public static Product create(Long id, String name, UseClass useClass, ProductUnit unit, String concentrationActiveIngredient, Long registerNumber, String company, ToxiClass toxiClass, String activeIngredient) {
        Product instance = new Product();
        instance.setId(id);
        instance.setName(name);
        instance.setUseClass(useClass);
        instance.setUnit(unit);
        instance.setConcentrationActiveIngredient(concentrationActiveIngredient);
        instance.setRegisterNumber(registerNumber);
        instance.setCompany(company);
        instance.setToxiClass(toxiClass);
        instance.setActiveIngredient(activeIngredient);

        return instance;
    }
    
    public String getUnitDescription() {
        if (this.getUnit() != null)
            return this.getUnit().getDescription();
        
        return null;
    }
    
    public String getUseClassDescription() {
        if (this.getUseClass() != null)
            return this.getUseClass().getDescription();
        
        return null;
    }
    
    public String getToxiClassDescription() {
        if (this.getToxiClass() == null)
            return "";
        
        else
            return this.getToxiClass().getDescription();
    }
    

}
