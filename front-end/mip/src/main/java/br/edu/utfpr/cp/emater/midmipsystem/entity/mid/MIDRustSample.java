package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.AuditingPersistenceEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MIDRustSample extends AuditingPersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @ManyToOne
    private Survey survey;

    @EqualsAndHashCode.Include
    @Temporal(TemporalType.DATE)
    @NotNull (message = "A data da coleta precisa ser informada!")
    private Date sampleDate;
    
    @Embedded
    private MIDSampleSporeCollectorOccurrence sporeCollectorOccurrence;
    
    @Embedded
    private MIDSampleLeafInspectionOccurrence leafInspectionOccurrence;
    
    @Embedded
    private MIDSampleFungicideApplicationOccurrence fungicideOccurrence;

    @Builder
    public static MIDRustSample create (Long id,
                                                  Survey  survey,
                                                  Date sampleDate) {
        var instance = new MIDRustSample();
        instance.setId(id);
        instance.setSurvey(survey);
        instance.setSampleDate(sampleDate);
        
        return instance;
    }
}
