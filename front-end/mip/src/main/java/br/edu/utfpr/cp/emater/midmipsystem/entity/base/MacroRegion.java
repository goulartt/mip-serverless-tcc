package br.edu.utfpr.cp.emater.midmipsystem.entity.base;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.text.WordUtils;

@Entity
@Getter
@Setter
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class MacroRegion extends AuditingPersistenceEntity implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @EqualsAndHashCode.Include
    @Size (min = 3, max = 50, message = "O nome da macrorregião deve ter entre 3 e 50 caracteres")
    private String name;
    
    public void setName (String name) {
        this.name = WordUtils.capitalize(name.toLowerCase());
    }

    @Builder
    public static MacroRegion create (Long id, String name) {
        MacroRegion instance = new MacroRegion();
        instance.setId(id);
        instance.setName(name);
        
        return instance;
    }
}