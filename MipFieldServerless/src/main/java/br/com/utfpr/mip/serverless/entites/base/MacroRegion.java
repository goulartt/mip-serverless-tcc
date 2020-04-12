package br.com.utfpr.mip.serverless.entites.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "macro_region")
@Getter
@Setter
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class MacroRegion implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
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
    public static MacroRegion create (Long id, String name) {
        MacroRegion instance = new MacroRegion();
        instance.setId(id);
        instance.setName(name);
        
        return instance;
    }
}