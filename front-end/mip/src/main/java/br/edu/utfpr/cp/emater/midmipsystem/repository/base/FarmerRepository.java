package br.edu.utfpr.cp.emater.midmipsystem.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    
}
