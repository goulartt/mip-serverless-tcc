package br.edu.utfpr.cp.emater.midmipsystem.repository.security;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{ 
    public Authority findByName (String name);
}
