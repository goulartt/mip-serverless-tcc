package br.com.utfpr.mip.serverless.entites;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.utfpr.mip.serverless.dto.base.FieldDTO.FieldDTOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity(name = "city")
@Getter
@Setter
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Table(name = "city")
public class City  implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @EqualsAndHashCode.Include
    private String name;
    
    @Enumerated (EnumType.STRING)
    @EqualsAndHashCode.Include
    private State state;
        
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
    public static City create (Long id, String name, State state) {
        City instance = new City();
        instance.setName(name);
        instance.setState(state);
        instance.setId(id);

        return instance;
    }
    
    public String getIdAsString() {
        return String.valueOf(this.getId());
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.getName(), this.getState());
    }
}