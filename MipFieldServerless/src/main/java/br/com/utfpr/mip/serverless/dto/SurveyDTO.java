package br.com.utfpr.mip.serverless.dto;

import java.io.Serializable;

import br.com.utfpr.mip.serverless.entites.base.Field;
import br.com.utfpr.mip.serverless.entites.survey.CropData;
import br.com.utfpr.mip.serverless.entites.survey.CultivarData;
import br.com.utfpr.mip.serverless.entites.survey.Harvest;
import br.com.utfpr.mip.serverless.entites.survey.LocationData;
import br.com.utfpr.mip.serverless.entites.survey.MIDData;
import br.com.utfpr.mip.serverless.entites.survey.ProductivityData;
import br.com.utfpr.mip.serverless.entites.survey.PulverisationData;
import br.com.utfpr.mip.serverless.entites.survey.SizeData;
import br.com.utfpr.mip.serverless.entites.survey.Survey;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SurveyDTO implements Serializable {
	
	private Long id;
	private Long fieldId;
	private Long harverstId;
	
    private CultivarData cultivarData;

    private CropData cropData;

    private SizeData sizeData;

    private LocationData locationData;

    private ProductivityData productivityData;
    
    private MIDData midData;

    private PulverisationData pulverisationData;

	private Long createdBy;
	private Long modifiedBy;
	
	public static Survey generateEntityFromDTO(SurveyDTO readValue) {
		return Survey.create(readValue.getId(), readValue.getCultivarData().getCultivarName(), readValue.getMidData().isSporeCollectorPresent(),
				readValue.getMidData().getCollectorInstallationDate(), readValue.getCultivarData().isRustResistant(), readValue.getCultivarData().isBt(), 
				readValue.getCropData().getSowedDate(), readValue.getCropData().getEmergenceDate(), readValue.getCropData().getHarvestDate(), 
				readValue.getLocationData().getLongitude(), readValue.getLocationData().getLatitude(), 
				readValue.getProductivityData().getProductivityField(), readValue.getProductivityData().getProductivityFarmer(), 
				readValue.getProductivityData().isSeparatedWeight(), readValue.getSizeData().getTotalArea(),
				readValue.getSizeData().getTotalPlantedArea(), readValue.getSizeData().getPlantPerMeter(), 
				readValue.getPulverisationData().getSoyaPrice(), readValue.getPulverisationData().getApplicationCostCurrency(), 
				Field.builder().id(readValue.getFieldId()).build(), Harvest.builder().id(readValue.getHarverstId()).build());
	
	}
}
