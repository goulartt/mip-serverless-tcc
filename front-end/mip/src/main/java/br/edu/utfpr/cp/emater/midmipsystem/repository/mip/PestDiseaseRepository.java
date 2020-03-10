package br.edu.utfpr.cp.emater.midmipsystem.repository.mip;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.PestDisease;

public interface PestDiseaseRepository extends JpaRepository<PestDisease, Long>{
    
}
