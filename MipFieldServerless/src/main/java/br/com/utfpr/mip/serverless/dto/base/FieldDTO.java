package br.com.utfpr.mip.serverless.dto.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.utfpr.mip.serverless.entites.City;
import br.com.utfpr.mip.serverless.entites.Farmer;
import br.com.utfpr.mip.serverless.entites.Field;
import br.com.utfpr.mip.serverless.entites.MIPUser;
import br.com.utfpr.mip.serverless.entites.Supervisor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FieldDTO implements Serializable {

	private Long id;

	private String name;
	
	private String location;

	private Long cityId;

	private Long farmerId;

	private Set<Long> supervisors;

	private Long createdBy;

	private Long modifiedBy;
	
	public static Field generateEntityFromDTO(FieldDTO dto) {
		
		Set<Supervisor> collect = dto.getSupervisors().stream().map(s -> {
			return Supervisor.builder().id(s).build();
		}).collect(Collectors.toSet());
		
		Field build = Field.builder()
		.id(dto.getId())
		.location(dto.getLocation())
		.name(dto.getName())
		.city(City.builder().id(dto.getCityId()).build())
		.farmer(Farmer.builder().id(dto.getFarmerId()).build())
		.supervisors(collect)
		.build();
		
		build.setCreatedAt(new Date().getTime());
		build.setCreatedBy(MIPUser.builder().id(dto.getCreatedBy()).build());
		build.setModifiedBy(MIPUser.builder().id(dto.getModifiedBy()).build());

		return build;
	}

	public static String generateJSON(FieldDTO newField) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(newField);
	}

}
