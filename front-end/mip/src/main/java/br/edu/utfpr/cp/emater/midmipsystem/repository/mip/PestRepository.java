package br.edu.utfpr.cp.emater.midmipsystem.repository.mip;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.Pest;

public interface PestRepository extends JpaRepository<Pest, Long>{

    public Optional<List<Pest>> findByScientificNameStartsWith(String aScientificName);
    
}
