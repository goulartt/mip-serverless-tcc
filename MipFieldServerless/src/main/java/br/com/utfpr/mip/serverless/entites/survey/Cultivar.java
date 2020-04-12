package br.com.utfpr.mip.serverless.entites.survey;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.utfpr.mip.serverless.entites.base.MIPUser;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class Cultivar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    private String name;
    
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
    public static Cultivar create (Long id, String name) {
        var instance = new Cultivar();
        instance.setId(id);
        instance.setName(name);
        
        return instance;
    }
}
