package br.edu.utfpr.cp.emater.midmipsystem.repository.mip;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.Pest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PestRepository extends JpaRepository<Pest, Long>{
    
}
