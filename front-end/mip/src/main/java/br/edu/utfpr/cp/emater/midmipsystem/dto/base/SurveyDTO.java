package br.edu.utfpr.cp.emater.midmipsystem.dto.base;

import java.io.Serializable;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.CropData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.CultivarData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.LocationData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.MIDData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.ProductivityData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.PulverisationData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.SizeData;
import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
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
	
	public static SurveyDTO generateFromEntity(Survey readValue) {
		return SurveyDTO.builder()
			.id(readValue.getId())
			.fieldId(readValue.getFieldId())
			.harverstId(readValue.getHarvestId())
			.createdBy(readValue.getCreatedBy().getId())
			.modifiedBy(readValue.getModifiedBy().getId())
			.sizeData(readValue.getSizeData())
			.locationData(readValue.getLocationData())
			.cultivarData(readValue.getCultivarData())
			.cropData(readValue.getCropData())
			.productivityData(readValue.getProductivityData())
			.midData(readValue.getMidData())
			.pulverisationData(readValue.getPulverisationData())
			.build();
		
	
	}
}
