package com.utfpr.serverless.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class City extends AuditingPersistenceEntity implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @EqualsAndHashCode.Include
    private String name;
    
    @Enumerated (EnumType.STRING)
    @EqualsAndHashCode.Include
    private State state;
        

    
    @Builder
    public static City create (String name, State state) {
        City instance = new City();
        instance.setName(name);
        instance.setState(state);
        
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