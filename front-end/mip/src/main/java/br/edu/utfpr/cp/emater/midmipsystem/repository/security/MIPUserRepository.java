package br.edu.utfpr.cp.emater.midmipsystem.repository.security;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MIPUserRepository extends JpaRepository<MIPUser, Long> { 
    
    MIPUser findByUsername (String username);
    
}
