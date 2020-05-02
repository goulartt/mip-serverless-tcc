package br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.AuditingPersistenceEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.GrowthPhase;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import br.edu.utfpr.cp.emater.midmipsystem.exception.ProductUseClassDifferFromTargetException;
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
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PulverisationOperation extends AuditingPersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @ManyToOne
    private Survey survey;

    @EqualsAndHashCode.Include
    @Temporal(TemporalType.DATE)
    @NotNull(message = "A data da coleta precisa ser informada!")
    private Date sampleDate;

    @Enumerated(EnumType.STRING)
    private GrowthPhase growthPhase;

    private double caldaVolume;

    @ElementCollection
    private Set<PulverisationOperationOccurrence> operationOccurrences;

    @Builder
    public static PulverisationOperation create(Long id,
            Survey survey,
            Date sampleDate,
            GrowthPhase growthPhase,
            double caldaVolume) {

        var instance = new PulverisationOperation();
        instance.setId(id);
        instance.setSurvey(survey);
        instance.setSampleDate(sampleDate);
        instance.setGrowthPhase(growthPhase);
        instance.setCaldaVolume(caldaVolume);

        return instance;
    }

    public PulverisationOperation() {
        super();

        this.setOperationOccurrences(new HashSet<>());
    }

    public boolean addOperationOccurrence(Product product, double productPrice, double productDose, Target target) throws ProductUseClassDifferFromTargetException {

        if (product.getUseClass() != target.getUseClass()) {
            throw new ProductUseClassDifferFromTargetException();

        } else {
            var occurrence = PulverisationOperationOccurrence.builder().product(product).productPrice(productPrice).dose(productDose).target(target).build();

            var result = this.getOperationOccurrences().add(occurrence);

            return result;
        }

    }

    public double getSoyaPrice() {
        if (this.getSurvey() != null) {
            if (this.getSurvey().getPulverisationData() != null) {
                return this.getSurvey().getPulverisationData().getSoyaPrice();
            }
        }

        return 0;
    }

    public double getApplicationCostCurrency() {
        if (this.getSurvey() != null) {
            if (this.getSurvey().getPulverisationData() != null) {
                return this.getSurvey().getPulverisationData().getApplicationCostCurrency();
            }
        }

        return 0;
    }

    public double getApplicationCostQty() {
        if (this.getSurvey() != null) {
            if (this.getSurvey().getPulverisationData() != null) {
                return this.getSurvey().getPulverisationData().getApplicationCostQty();
            }
        }

        return 0;
    }

    public double getTotalOperationCostCurrency() {
        if (this.getOperationOccurrences() != null) {
            if (this.getOperationOccurrences().size() != 0) {
                var totalCostWithProducts = this.getOperationOccurrences().stream().mapToDouble(occurrence -> occurrence.getProductCostCurrency()).sum();
                return totalCostWithProducts + this.getApplicationCostCurrency();

            }
        }

        return 0;
    }
}
