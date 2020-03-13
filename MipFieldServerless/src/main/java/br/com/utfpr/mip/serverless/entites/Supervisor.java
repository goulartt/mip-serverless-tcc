package br.com.utfpr.mip.serverless.entites;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "supervisor")
public class Supervisor extends Person implements Serializable {

	private String email;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@Builder
	private static Supervisor create(Long id, String name, String email, Region region) {
		Supervisor instance = new Supervisor();
		instance.setId(id);
		instance.setName(name);
		instance.setEmail(email);
		instance.setRegion(region);

		return instance;
	}
	
	@JsonIgnore
	public String getRegionName() {
		if (this.getRegion() != null)
			return this.getRegion().getName();

		return null;
	}
	@JsonIgnore
	public Long getRegionId() {
		if (this.getRegion() != null)
			return this.getRegion().getId();

		return null;
	}
	@JsonIgnore
	public String getMacroRegionName() {
		if (this.getRegion() != null)
			return this.getRegion().getMacroRegionName();

		return null;
	}
	@JsonIgnore
	public Long getMacroRegionId() {
		if (this.getRegion() != null)
			return this.getRegion().getMacroRegionId();

		return null;
	}
	@JsonIgnore
	public String getIdAsString() {
		if (this.getId() != null)
			return String.valueOf(this.getId());

		return null;
	}
	@JsonIgnore
	public String getCitiesInRegionNames() {
		if (this.getRegion() != null)
			return this.getRegion().getCities().toString();

		return null;
	}
	@JsonIgnore
	public Optional<MacroRegion> getMacroRegion() {

		if (this.getRegion() == null)
			return Optional.empty();

		if (this.getRegion().getMacroRegion() == null)
			return Optional.empty();

		return Optional.of(this.getRegion().getMacroRegion());
	}
}
