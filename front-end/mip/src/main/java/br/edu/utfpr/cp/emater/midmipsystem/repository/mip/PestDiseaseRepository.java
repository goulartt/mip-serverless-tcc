package br.edu.utfpr.cp.emater.midmipsystem.repository.mip;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.PestDisease;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PestDiseaseRepository extends JpaRepository<PestDisease, Long>{
    
}
