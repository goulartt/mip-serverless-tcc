package br.edu.utfpr.cp.emater.midmipsystem.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Cultivar;

public interface CultivarRepository extends JpaRepository<Cultivar, Long> { 

    List<Cultivar> findByNameContainingIgnoreCase (String excerpt);
}
