package br.edu.utfpr.cp.emater.midmipsystem.entity.security;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Region;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    
    @Size (min = 5, message = "O nome completo deve ser maior que 5 caracteres")
    private String fullName;
    
    @EqualsAndHashCode.Include
    @Email (message = "Deve ser informado um e-mail válido")
    private String email;
    
    @Size (min = 3, max = 30, message = "O nome de usuário deve ter entre 3 e 20 caracteres")
    @EqualsAndHashCode.Include
    private String username;
    
    @Size (min = 3, message = "A senha deve ser maior que 3 caracteres")
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
