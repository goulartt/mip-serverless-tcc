package br.edu.utfpr.cp.emater.midmipsystem.entity.mip;

import br.edu.utfpr.cp.emater.midmipsystem.service.analysis.DAEAndOccurrenceDTO;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.AuditingPersistenceEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
        if (this.getSurvey() != null) {
            return this.getSurvey().getHarvestName();
        }

        return null;
    }

    public Optional<Long> getHarvestId() {
        if (this.getSurvey() == null) {
            return Optional.empty();
        }

        if (this.getSurvey().getHarvest() == null) {
            return Optional.empty();
        }

        return Optional.of(this.getSurvey().getHarvest().getId());
    }

    public String getFarmerName() {
        if (this.getSurvey() != null) {
            return this.getSurvey().getFarmerString();
        }

        return null;
    }

    public String getFieldName() {
        if (this.getSurvey() != null) {
            return this.getSurvey().getFieldName();
        }

        return null;
    }

    public String getCityName() {
        if (this.getSurvey() != null) {
            return this.getSurvey().getFieldCityName();
        }

        return null;
    }

    public String getSupervisorNames() {
        if (this.getSurvey() != null) {
            return this.getSurvey().getField().getSupervisorNames().toString();
        }

        return null;
    }

    public String getCultivarName() {
        if (this.getSurvey() != null) {
            return this.getSurvey().getCultivarName();
        }

        return null;
    }

    public int getDAE() {
        if (this.getSurvey() == null) {
            return 0;
        }

        if (this.getSurvey().getEmergenceDate() == null) {
            return 0;
        }

        if (this.getSampleDate() == null) {
            return 0;
        }

        long diffInMillies = (this.getSampleDate().getTime() - this.getSurvey().getEmergenceDate().getTime());

        if (diffInMillies > 0) {
            var result = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            return (int) (result + 1);

        } else {
            return 0;

        }
    }

    public Optional<MIPSamplePestOccurrence> getOccurrenceByPest(Pest aPest) {

        if (aPest == null) {
            return Optional.empty();
        }

        return this.getMipSamplePestOccurrence()
                .stream()
                .filter(currentOccurrence -> currentOccurrence.getPest().equals(aPest))
                .findFirst();
    }

    public Optional<MIPSampleNaturalPredatorOccurrence> getOccurrenceByPredator(PestNaturalPredator aPredator) {

        if (aPredator == null) {
            return Optional.empty();
        }

        return this.getMipSampleNaturalPredatorOccurrence()
                .stream()
                .filter(currentOccurrence -> currentOccurrence.getPestNaturalPredator().equals(aPredator))
                .findAny();
    }

    public double getOccurrenceValueByPest(Pest aPest) {

        if (aPest == null) {
            return 0.0;
        }

        var occurrence = this.getOccurrenceByPest(aPest);

        if (occurrence.isEmpty()) {
            return 0.0;
        }

        if (occurrence.isPresent()) {
            return occurrence.get().getValue();
        }

        return 0.0;
    }

    public double getOccurrenceValueByPredator(PestNaturalPredator aPredator) {
        if (aPredator == null) {
            return 0.0;
        }

        var occurrence = this.getOccurrenceByPredator(aPredator);

        if (occurrence.isEmpty()) {
            return 0.0;
        }

        if (occurrence.isPresent()) {
            return occurrence.get().getValue();
        }
        
        return 0.0;
    }
    
    public Optional<DAEAndOccurrenceDTO> getDAEAndPredatorOccurrenceByPredator(PestNaturalPredator aPredator) {
        
        if (aPredator == null) {
            return Optional.empty();
        }
        
        var occurrenceByPredator = this.getOccurrenceByPredator(aPredator);
        
        if (occurrenceByPredator.isPresent()) {
            return Optional.of(DAEAndOccurrenceDTO.builder().dae(this.getDAE()).occurrence(occurrenceByPredator.get().getValue()).build());
        }
        
        return Optional.empty();
    }

    public Optional<DAEAndOccurrenceDTO> getDAEAndPestOccurrenceByPest(Pest aPest) {

        if (aPest == null) {
            return Optional.empty();
        }

        var occurrenceByPest = this.getOccurrenceByPest(aPest);

        if (occurrenceByPest.isPresent()) {
            return Optional.of(DAEAndOccurrenceDTO.builder().dae(this.getDAE()).occurrence(occurrenceByPest.get().getValue()).build());
        }

        return Optional.empty();
    }

    public Optional<Long> getFieldId() {

        if (this.getSurvey() == null) {
            return Optional.empty();
        }

        if (this.getSurvey().getField() == null) {
            return Optional.empty();
        }

        return Optional.of(this.getSurvey().getField().getId());
    }

    public Optional<City> getCity() {

        if (this.getSurvey() == null) {
            return Optional.empty();
        }

        if (this.getSurvey().getField() == null) {
            return Optional.empty();
        }

        if (this.getSurvey().getField().getCity() == null) {
            return Optional.empty();
        }

        return Optional.of(this.getSurvey().getField().getCity());
    }

}
