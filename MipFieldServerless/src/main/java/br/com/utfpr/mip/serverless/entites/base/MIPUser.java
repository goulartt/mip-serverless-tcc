package br.com.utfpr.mip.serverless.entites.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity (name = "mipuser")
public class MIPUser implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "full_name")
    private String fullName;
    
    @EqualsAndHashCode.Include
    @JsonIgnore
    private String email;
    
    @EqualsAndHashCode.Include
    @JsonIgnore
    private String username;
    
    @JsonIgnore
    private String password;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "region_id")
    private Region region;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "city_id")   
    private City city;
    
	@Column(name = "account_non_expired")   
    private boolean accountNonExpired;
	
	@Column(name = "account_non_locked")   
    private boolean accountNonLocked;
    
	@Column(name = "credentials_non_expired")   
    private boolean credentialsNonExpired;
    
    private boolean enabled = true;
    
    @ManyToMany
    @JsonIgnore
    private List<Authority> authorities;
    
    @JsonIgnore
    public String getRegionName() {
        if (this.getRegion() != null)
            return this.getRegion().getName();
        
        else
            return null;
    }
    
    @JsonIgnore
    public String getCityName() {
        if (this.getCity() != null)
            return this.getCity().getName();
        
        else
            return null;
    }
    
    @JsonIgnore
    public Long getRegionId() {
        if (this.getRegion() != null)
            return this.getRegion().getId();
        
        else
            return null;
    }
    
    @JsonIgnore
    public Long getCityId() {
        
        if (this.getCity() != null)
            return this.getCity().getId();
        
        else
            return null;
    }
}
