package br.edu.utfpr.cp.emater.midmipsystem.repository.base;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

    public Optional<Supervisor> findByEmail(String anEmail);
    
}
