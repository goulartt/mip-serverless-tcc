package br.edu.utfpr.cp.emater.midmipsystem.entity.survey;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MIDData implements Serializable {

    private boolean sporeCollectorPresent;
    
    @Temporal (TemporalType.DATE)
    private Date collectorInstallationDate;
}
