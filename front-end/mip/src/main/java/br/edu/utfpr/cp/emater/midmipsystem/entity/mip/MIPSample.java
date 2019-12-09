package br.edu.utfpr.cp.emater.midmipsystem.entity.mip;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.AuditingPersistenceEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MIPSample extends AuditingPersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Temporal(TemporalType.DATE)
    private Date sampleDate;

    private int daysAfterEmergence;
    private int defoliation;

    @Enumerated(EnumType.STRING)
    private GrowthPhase growthPhase;

    @ElementCollection
    private Set<MIPSamplePestOccurrence> mipSamplePestOccurrence;

    @ElementCollection
    private Set<MIPSamplePestDiseaseOccurrence> mipSamplePestDiseaseOccurrence;

    @ElementCollection
    private Set<MIPSampleNaturalPredatorOccurrence> mipSampleNaturalPredatorOccurrence;

    @EqualsAndHashCode.Include
    @ManyToOne
    private Survey survey;

    @Builder
    public static MIPSample create(Long id,
            Date sampleDate,
            int daysAfterEmergence,
            int defoliation,
            GrowthPhase growthPhase,
            Survey survey) {

        var instance = new MIPSample();
        instance.setId(id);
        instance.setSampleDate(sampleDate);
        instance.setDaysAfterEmergence(daysAfterEmergence);
        instance.setDefoliation(defoliation);
        instance.setGrowthPhase(growthPhase);
        instance.setSurvey(survey);

        return instance;
    }

    public boolean addPestOccurrence(Pest pest, double value) {

        if (this.getMipSamplePestOccurrence() == null) {
            this.setMipSamplePestOccurrence(new HashSet<>());
        }

        return this.getMipSamplePestOccurrence().add(MIPSamplePestOccurrence.builder().pest(pest).value(value).build());
    }

    public boolean addPestDiseaseOccurrence(PestDisease pestDisease, double value) {

        if (this.getMipSamplePestDiseaseOccurrence() == null) {
            this.setMipSamplePestDiseaseOccurrence(new HashSet<>());
        }

        return this.getMipSamplePestDiseaseOccurrence().add(MIPSamplePestDiseaseOccurrence.builder().pestDisease(pestDisease).value(value).build());
    }
    
    public boolean addPestNaturalPredatorOccurrence(PestNaturalPredator pestNaturalPredator, double value) {

        if (this.getMipSampleNaturalPredatorOccurrence() == null) {
            this.setMipSampleNaturalPredatorOccurrence(new HashSet<>());
        }

        return this.getMipSampleNaturalPredatorOccurrence().add(MIPSampleNaturalPredatorOccurrence.builder().pestNaturalPredator(pestNaturalPredator).value(value).build());
    }    

    public String getHarvestName() {
        return this.getSurvey().getHarvestName();
    }

    public String getFarmerName() {
        return this.getSurvey().getFarmerString();
    }

    public String getFieldName() {
        return this.getSurvey().getFieldName();
    }

    public String getCityName() {
        return this.getSurvey().getFieldCityName();
    }

    public String getSupervisorNames() {
        return this.getSurvey().getField().getSupervisorNames().toString();
    }

    public String getSeedName() {
        return this.getSurvey().getSeedName();
    }
}
