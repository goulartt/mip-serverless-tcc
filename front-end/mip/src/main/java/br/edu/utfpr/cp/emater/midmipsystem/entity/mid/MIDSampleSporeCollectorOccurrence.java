package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
public class MIDSampleSporeCollectorOccurrence implements Serializable {
        
    private boolean bladeInstalledPreCold;
    
    @ManyToOne
    private BladeReadingResponsiblePerson bladeReadingResponsiblePerson;
    
    @Temporal(TemporalType.DATE)
    private Date bladeReadingDate;
    
    @Enumerated(EnumType.STRING)
    private AsiaticRustTypesSporeCollector bladeReadingRustResultCollector;
}
