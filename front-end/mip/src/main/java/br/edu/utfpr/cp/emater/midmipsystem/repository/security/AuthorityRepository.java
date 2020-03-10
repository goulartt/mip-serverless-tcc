package br.edu.utfpr.cp.emater.midmipsystem.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{ 
    public Authority findByName (String name);
}
