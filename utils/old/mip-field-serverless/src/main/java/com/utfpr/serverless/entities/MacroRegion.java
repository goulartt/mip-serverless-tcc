package com.utfpr.serverless.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class MacroRegion extends AuditingPersistenceEntity implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @EqualsAndHashCode.Include
    private String name;
    


    @Builder
    public static MacroRegion create (Long id, String name) {
        MacroRegion instance = new MacroRegion();
        instance.setId(id);
        instance.setName(name);
        
        return instance;
    }
}