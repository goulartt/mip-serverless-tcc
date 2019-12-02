package br.edu.utfpr.cp.emater.midmipsystem.repository.base;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Supervisor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

    public Optional<Supervisor> findByEmail(String anEmail);
    
}
