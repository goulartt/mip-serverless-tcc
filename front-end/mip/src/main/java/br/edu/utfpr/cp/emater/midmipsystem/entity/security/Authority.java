package br.edu.utfpr.cp.emater.midmipsystem.entity.security;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Authority implements Serializable {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Size (min = 3, message = "O nome precisa ter pelo menos 3 caracteres")
    private String name;
    
    @Size (min = 8, message = "A descrição ter pelo menos 8 caracteres")
    private String description;
        
    @ManyToMany (mappedBy = "authorities")
    private List<MIPUser> users;
}
