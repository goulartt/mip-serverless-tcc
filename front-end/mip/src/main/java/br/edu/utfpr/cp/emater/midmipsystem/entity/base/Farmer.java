package br.edu.utfpr.cp.emater.midmipsystem.entity.base;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Farmer extends Person implements Serializable {

    public Farmer () {
        super();
    }

    @Builder
    public static Farmer create (Long id, String name) {
        Farmer instance = new Farmer();

        instance.setId(id);
        instance.setName(name);
        
        return instance;
    }
    
}
