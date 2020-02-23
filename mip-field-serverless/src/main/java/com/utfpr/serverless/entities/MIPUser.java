package com.utfpr.serverless.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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
@Entity (name = "MIPUSER")
public class MIPUser implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fullName;
    
    @EqualsAndHashCode.Include
    private String email;
    
    @EqualsAndHashCode.Include
    private String username;
    
    private String password;
    
    @ManyToOne
    private Region region;
    
    @ManyToOne
    private City city;
    
    private boolean accountNonExpired;
    
    private boolean accountNonLocked;
    
    private boolean credentialsNonExpired;
    
    private boolean enabled = true;
    
    @ManyToMany (fetch = FetchType.EAGER)
    private List<Authority> authorities;
    
    public String getRegionName() {
        if (this.getRegion() != null)
            return this.getRegion().getName();
        
        else
            return null;
    }
    
    public String getCityName() {
        if (this.getCity() != null)
            return this.getCity().getName();
        
        else
            return null;
    }
    
    public Long getRegionId() {
        if (this.getRegion() != null)
            return this.getRegion().getId();
        
        else
            return null;
    }

    public Long getCityId() {
        
        if (this.getCity() != null)
            return this.getCity().getId();
        
        else
            return null;
    }
}
