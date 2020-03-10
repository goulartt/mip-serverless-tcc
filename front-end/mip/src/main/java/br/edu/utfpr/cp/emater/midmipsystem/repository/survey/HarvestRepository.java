package br.edu.utfpr.cp.emater.midmipsystem.repository.survey;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Harvest;

public interface HarvestRepository extends JpaRepository<Harvest, Long>{
    
}
