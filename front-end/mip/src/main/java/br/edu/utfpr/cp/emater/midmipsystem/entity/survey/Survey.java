package br.edu.utfpr.cp.emater.midmipsystem.entity.survey;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.AuditingPersistenceEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.State;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Builder;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class Survey extends AuditingPersistenceEntity implements Serializable {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
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
    
    @ManyToOne (fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    @NotNull (message = "Uma pesquisa deve conter uma unidade de referência")
    private Field field;
    
    @ManyToOne (fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    @NotNull (message = "Uma pesquisa deve ser referente a uma safra")
    private Harvest harvest;

    
    @Builder
    public static Survey create (Long id, 
                                 String cultivarName, 
                                 boolean sporeCollectorPresent, 
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
                                 Field field,
                                 Harvest harvest) {
                
        var instance = new Survey();
        instance.setId(id);
        instance.setField(field);
        instance.setHarvest(harvest);
        
        instance.setCropData(CropData.builder().emergenceDate(emergenceDate).harvestDate(harvestDate).sowedDate(sowedDate).build());
        instance.setLocationData(LocationData.builder().latitude(latitude).longitude(longitude).build());
        instance.setProductivityData(ProductivityData.builder().productivityFarmer(productivityFarmer).productivityField(productivityField).separatedWeight(separatedWeight).build());
        instance.setCultivarData(CultivarData.builder().name(cultivarName).bt(bt).rustResistant(rustResistant).build());
        instance.setSizeData(SizeData.builder().plantPerMeter(plantPerMeter).totalArea(totalArea).totalPlantedArea(totalPlantedArea).build());
        instance.setMidData(MIDData.builder().sporeCollectorPresent(sporeCollectorPresent).build());
        
        return instance;
    }
    
    public String getSeedName() {
        return this.getCultivarData().getName();
    }
    
    public String getCultivarName() {
        return this.getCultivarData().getName();
    }
    
    public boolean isRustResistant() {
        return this.getCultivarData().isRustResistant();
    }
    
    public boolean isBt() {
        return this.getCultivarData().isBt();
    }
    
    public Date getSowedDate() {
        if (this.getCropData() == null)
            return this.getHarvest().getBegin();
        
        else if (this.getCropData().getSowedDate() == null)
            return this.getHarvest().getBegin();
        
        else
            return this.getCropData().getSowedDate();
    }
    
    public Date getEmergenceDate() {
        if (this.getCropData() == null)
            return this.getHarvest().getBegin();
        
        else if (this.getCropData().getEmergenceDate() == null)
            return this.getHarvest().getBegin();
        
        else
            return this.getCropData().getEmergenceDate();
    }
    
    public Date getHarvestDate() {
        if (this.getCropData() == null)
            return this.getHarvest().getBegin();
        
        else if (this.getCropData().getHarvestDate() == null)
            return this.getHarvest().getBegin();
        
        else
            return this.getCropData().getHarvestDate();
    }
    
    public String getLongitude() {
        return this.getLocationData().getLongitude();
    }
    
    public String getLatitude() {
        return this.getLocationData().getLatitude();
    }
    
    public double getProductivityField() {
        return this.getProductivityData().getProductivityField();
    }
    
    public double getProductivityFarmer() {
        return this.getProductivityData().getProductivityFarmer();
    }
    
    public boolean isSeparatedWeight() {
        return this.getProductivityData().isSeparatedWeight();
    }
    
    public double getTotalArea() {
        return this.getSizeData().getTotalArea();
    }
    
    public double getTotalPlantedArea() {
        return this.getSizeData().getTotalPlantedArea();
    }
    
    public double getPlantPerMeter() {
        return this.getSizeData().getPlantPerMeter();
    }
    
    public Long getFieldId() {
        return this.getField().getId();
    }
    
    public String getFieldName() {
        return this.getField().getName();
    }
    
    public String getFieldLocation() {
        return this.getField().getLocation();
    }
    
    public Long getFieldCityId() {
        return this.getField().getCityId();
    }
    
    public String getFieldCityName() {
        return this.getField().getCityName();
    }
    
    public State getFieldCityState() {
        return this.getField().getCity().getState();
    }
    
    public Long getFarmerId() {
        return this.getField().getFarmerId();
    }   
    
    public String getFarmerString() {
        return this.getField().getFarmerName();
    }
    
    public Set<Supervisor> getFieldSupervisors() {
        return this.getField().getSupervisors();
    }
    
    public Long getHarvestId() {
        return this.getHarvest().getId();
    }
    
    public String getHarvestName() {
        return this.getHarvest().getName();
    }
    
    public Date getHarvestBeginDate() {
        return this.getHarvest().getBegin();
    }
    
    public Date getHarvestEndDate() {
        return this.getHarvest().getEnd();
    }
    
    public String getFieldSupervisorNames() {
        return this.getField().getSupervisorNames().toString();
    }
    
    public boolean isSporeCollectorPresent() {
        return this.getMidData().isSporeCollectorPresent();
    }
    
}
