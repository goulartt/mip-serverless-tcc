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
public class MIPSampleNaturalPredatorOccurrence implements Serializable {
    
    protected double value;
    
    @ManyToOne (fetch = FetchType.EAGER)
    protected PestNaturalPredator pestNaturalPredator;
    
    public String getPestNaturalPredatorUsualName() {
        if (this.getPestNaturalPredator() != null)
            return this.getPestNaturalPredator().getUsualName();
        
        return null;
    } 
}
