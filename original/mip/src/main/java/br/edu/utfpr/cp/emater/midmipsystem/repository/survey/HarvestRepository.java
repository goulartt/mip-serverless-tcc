package br.edu.utfpr.cp.emater.midmipsystem.repository.survey;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarvestRepository extends JpaRepository<Harvest, Long>{
    
}
