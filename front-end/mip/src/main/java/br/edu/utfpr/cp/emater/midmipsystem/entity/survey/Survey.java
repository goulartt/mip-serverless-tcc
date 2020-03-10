package br.edu.utfpr.cp.emater.midmipsystem.entity.survey;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.AuditingPersistenceEntity;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.MacroRegion;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Region;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.State;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Survey extends AuditingPersistenceEntity implements Serializable {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    @NotNull(message = "Uma pesquisa deve conter uma unidade de referência")
    private Field field;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    @NotNull(message = "Uma pesquisa deve ser referente a uma safra")
    private Harvest harvest;

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

    public String getCultivarName() {
        if (this.getCultivarData() != null) {
            return this.getCultivarData().getCultivarName();
        }

        return null;
    }

    public boolean isRustResistant() {
        if (this.getCultivarData() != null) {
            return this.getCultivarData().isRustResistant();
        }

        return false;
    }

    public boolean isBt() {
        if (this.getCultivarData() != null) {
            return this.getCultivarData().isBt();
        }

        return false;
    }

    public Date getSowedDate() {
        if (this.getCropData() != null) {
            return this.getCropData().getSowedDate();
        }

        return null;
    }

    public Date getEmergenceDate() {
        if (this.getCropData() != null) {
            return this.getCropData().getEmergenceDate();
        }

        return null;
    }

    public Date getHarvestDate() {
        if (this.getCropData() != null) {
            return this.getCropData().getHarvestDate();
        }

        return null;
    }

    public String getLongitude() {
        if (this.getLocationData() != null) {
            return this.getLocationData().getLongitude();
        }

        return null;
    }

    public String getLatitude() {
        if (this.getLocationData() != null) {
            return this.getLocationData().getLatitude();
        }

        return null;
    }

    public double getProductivityField() {
        if (this.getProductivityData() != null) {
            return this.getProductivityData().getProductivityField();
        }

        return 0;
    }

    public double getProductivityFarmer() {
        if (this.getProductivityData() != null) {
            return this.getProductivityData().getProductivityFarmer();
        }

        return 0;
    }

    public boolean isSeparatedWeight() {
        if (this.getProductivityData() != null) {
            return this.getProductivityData().isSeparatedWeight();
        }

        return false;
    }

    public double getTotalArea() {
        if (this.getSizeData() != null) {
            return this.getSizeData().getTotalArea();
        }

        return 0;
    }

    public double getTotalPlantedArea() {
        if (this.getSizeData() != null) {
            return this.getSizeData().getTotalPlantedArea();
        }

        return 0;
    }

    public double getPlantPerMeter() {
        if (this.getSizeData() != null) {
            return this.getSizeData().getPlantPerMeter();
        }

        return 0;
    }

    public Long getFieldId() {
        if (this.getField() != null) {
            return this.getField().getId();
        }

        return null;
    }

    public String getFieldName() {
        if (this.getField() != null) {
            return this.getField().getName();
        }

        return null;
    }

    public String getFieldLocation() {
        if (this.getField() != null) {
            return this.getField().getLocation();
        }

        return null;
    }

    public Long getFieldCityId() {
        if (this.getField() != null) {
            return this.getField().getCityId();
        }

        return null;
    }

    public String getFieldCityName() {
        if (this.getField() != null) {
            return this.getField().getCityName();
        }

        return null;
    }

    public State getFieldCityState() {
        if (this.getField() != null) {
            return this.getField().getCity().getState();
        }

        return null;
    }

    public Long getFarmerId() {
        if (this.getField() != null) {
            return this.getField().getFarmerId();
        }

        return null;
    }

    public String getFarmerString() {
        if (this.getField() != null) {
            return this.getField().getFarmerName();
        }

        return null;
    }

    public Set<Supervisor> getFieldSupervisors() {
        if (this.getField() != null) {
            return this.getField().getSupervisors();
        }

        return null;
    }

    public Long getHarvestId() {
        if (this.getHarvest() != null) {
            return this.getHarvest().getId();
        }

        return null;
    }

    public String getHarvestName() {
        if (this.getHarvest() != null) {
            return this.getHarvest().getName();
        }

        return null;
    }

    public Date getHarvestBeginDate() {
        if (this.getHarvest() != null) {
            return this.getHarvest().getBegin();
        }

        return null;
    }

    public Date getHarvestEndDate() {
        if (this.getHarvest() != null) {
            return this.getHarvest().getEnd();
        }

        return null;
    }

    public String getFieldSupervisorNames() {
        if (this.getField() != null) {
            return this.getField().getSupervisorNames().toString();
        }

        return null;
    }

    public boolean isSporeCollectorPresent() {
        if (this.getMidData() != null) {
            return this.getMidData().isSporeCollectorPresent();
        }

        return false;
    }

    public Date getCollectorInstallationDate() {
        if (this.getMidData() != null) {
            return this.getMidData().getCollectorInstallationDate();
        }

        return null;
    }

    public double getSoyaPrice() {
        if (this.getPulverisationData() != null) {
            return this.getPulverisationData().getSoyaPrice();
        }

        return 0;
    }

    public double getApplicationCostCurrency() {
        if (this.getPulverisationData() != null) {
            return this.getPulverisationData().getApplicationCostCurrency();
        }

        return 0;
    }

    public Optional<MacroRegion> getMacroRegion() {

        if (this.getField() == null) {
            return Optional.empty();
        }

        if (this.getField().getMacroRegion().isPresent())
            return this.getField().getMacroRegion();
                
        return Optional.empty();
    }
    
    public Optional<Region> getRegion() {

        if (this.getField() == null) {
            return Optional.empty();
        }

        if (this.getField().getRegion().isPresent())
            return this.getField().getRegion();
                
        return Optional.empty();
    }

    public Optional<City> getCity() {
        
        if (this.getField() == null)
            return Optional.empty();
        
        if (this.getField().getCity() != null)
            return Optional.of(this.getField().getCity());
        
        return Optional.empty();
    }

}
