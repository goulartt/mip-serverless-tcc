package br.com.utfpr.mip.serverless.entites.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "region")
public class Region implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@EqualsAndHashCode.Include
	private String name;

	@EqualsAndHashCode.Include
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "macro_region_id")
	private MacroRegion macroRegion;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "region_cities", joinColumns = @JoinColumn(name = "region_id"), inverseJoinColumns = @JoinColumn(name = "cities_id"))
	private Set<City> cities;

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
	public static Region create(Long id, String name, MacroRegion macroRegion, Set<City> cities) {
		Region result = new Region();
		result.setId(id);
		result.setName(name);
		result.setMacroRegion(macroRegion);
		result.setCities(cities);

		return result;
	}

	public boolean addCity(City city) {

		if (!isThereACityContainer())
			createCityContainer();

		return this.getCities().add(city);
	}

	public boolean removeCity(City city) {
		if (isThereACityContainer())
			return this.getCities().remove(city);

		return false;
	}
	
	
	private boolean isThereACityContainer() {
		return !(this.getCities() == null);
	}

	private void createCityContainer() {
		this.setCities(new HashSet<City>());
	}
	
	@JsonIgnore
	public String getMacroRegionName() {
		if (this.getMacroRegion() != null)
			return this.getMacroRegion().getName();

		return null;
	}

	@JsonIgnore
	public Long getMacroRegionId() {
		if (this.getMacroRegion() != null)
			return this.getMacroRegion().getId();

		return null;
	}
	
	@JsonIgnore
	public Set<String> getCityNames() {
		if (this.getCities() != null)
			return this.getCities().stream().map(City::getName).collect(Collectors.toSet());

		return null;
	}
}
