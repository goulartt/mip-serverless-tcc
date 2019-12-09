package com.amazonaws.lambda.field.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.text.WordUtils;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MacroRegion implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @EqualsAndHashCode.Include
    private String name;
    
    public void setName (String name) {
        this.name = WordUtils.capitalize(name.toLowerCase());
    }

    @Builder
    public static MacroRegion create (Long id, String name) {
        MacroRegion instance = new MacroRegion();
        instance.setId(id);
        instance.setName(name);
        
        return instance;
    }
}