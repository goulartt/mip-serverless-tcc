package br.edu.utfpr.cp.emater.midmipsystem.dto.base;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
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

	public static String jsonFromEntity(Field field) {
		try {
			return new ObjectMapper().writeValueAsString(FieldDTO.builder().id(field.getId())
					.location(field.getLocation()).name(field.getName())
					.supervisors(field.getSupervisors().stream().map(Supervisor::getId).collect(Collectors.toSet()))
					.farmerId(field.getFarmerId()).cityId(field.getCityId()).createdBy(field.getCreatedBy().getId())
					.modifiedBy(field.getModifiedBy().getId()).build()

			);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
