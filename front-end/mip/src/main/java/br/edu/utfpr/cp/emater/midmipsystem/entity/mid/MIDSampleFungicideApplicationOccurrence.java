package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
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
public class MIDSampleFungicideApplicationOccurrence implements Serializable {

    private boolean asiaticRustApplication;
    private boolean otherDiseasesApplication;
    
    @Temporal (TemporalType.DATE)
    private Date fungicideApplicationDate;
    private String notes;
}
