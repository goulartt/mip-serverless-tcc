package br.edu.utfpr.cp.emater.midmipsystem.entity.mip;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MIPSamplePestOccurrence implements Serializable {

    protected double value;

    @ManyToOne(fetch = FetchType.EAGER)
    protected Pest pest;
    
    public String getPestUsualName() {
        if (this.getPest() != null)
            return this.getPest().getUsualName();
        
        return null;
    }
    
    public String getPestScientificName() {
        if (this.getPest() != null)
            return this.getPest().getScientificName();
        
        return null;
    }
    
    public String getPestSizeName() {
        if (this.getPest() != null)
            return this.getPest().getPestSizeName();
        
        return null;
    }
}
