package br.com.utfpr.mip.serverless.entites.survey;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.utfpr.mip.serverless.entites.base.Field;
import br.com.utfpr.mip.serverless.entites.base.MIPUser;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Survey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CultivarData cultivarData;

    @Embedded
    private CropData cropData;

    @Embedded
    private SizeData sizeData;

    @Embedded
    private LocationData locationData;

    @Embedded
    private ProductivityData productivityData;

    @Embedded
    private MIDData midData;

    @Embedded
    private PulverisationData pulverisationData;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Include
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    @JoinColumn(name = "harvest_id")
    private Harvest harvest;
    
    @Column(name = "created_at")
    private Long createdAt;
    
    @Column(name = "last_modified")
    private Long lastModified;
    
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private MIPUser createdBy;
    
    @ManyToOne
    @JoinColumn(name = "modified_by_id")
    private MIPUser modifiedBy;
    
    @Builder
    public static Survey create(Long id,
            String cultivarName,
            boolean sporeCollectorPresent,
            Date collectorInstallationDate,
            boolean rustResistant,
            boolean bt,
            Date sowedDate,
            Date emergenceDate,
            Date harvestDate,
            String longitude,
            String latitude,
            double productivityField,
            double productivityFarmer,
            boolean separatedWeight,
            double totalArea,
            double totalPlantedArea,
            double plantPerMeter,
            double soyaPrice,
            double applicationCostCurrency,
            Field field,
            Harvest harvest) {

        var instance = new Survey();
        instance.setId(id);
        instance.setField(field);
        instance.setHarvest(harvest);

        instance.setCropData(CropData.builder().emergenceDate(emergenceDate).harvestDate(harvestDate).sowedDate(sowedDate).build());
        instance.setLocationData(LocationData.builder().latitude(latitude).longitude(longitude).build());
        instance.setProductivityData(ProductivityData.builder().productivityFarmer(productivityFarmer).productivityField(productivityField).separatedWeight(separatedWeight).build());
        instance.setCultivarData(CultivarData.builder().cultivarName(cultivarName).bt(bt).rustResistant(rustResistant).build());
        instance.setSizeData(SizeData.builder().plantPerMeter(plantPerMeter).totalArea(totalArea).totalPlantedArea(totalPlantedArea).build());
        instance.setMidData(MIDData.builder().sporeCollectorPresent(sporeCollectorPresent).collectorInstallationDate(collectorInstallationDate).build());
        instance.setPulverisationData(PulverisationData.builder().soyaPrice(soyaPrice).applicationCostCurrency(applicationCostCurrency).build());

        return instance;
    }

}
