package br.com.utfpr.mip.serverless.entites;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "farmer")
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
