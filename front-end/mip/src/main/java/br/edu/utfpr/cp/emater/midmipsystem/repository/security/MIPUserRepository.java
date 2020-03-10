package br.edu.utfpr.cp.emater.midmipsystem.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUser;

public interface MIPUserRepository extends JpaRepository<MIPUser, Long> { 
    
    MIPUser findByUsername (String username);
    
}
