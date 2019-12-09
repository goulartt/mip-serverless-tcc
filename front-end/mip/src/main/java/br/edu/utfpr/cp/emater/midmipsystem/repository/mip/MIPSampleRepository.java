package br.edu.utfpr.cp.emater.midmipsystem.repository.mip;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.MIPSample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MIPSampleRepository extends JpaRepository<MIPSample, Long>{
    
}
