package br.edu.utfpr.cp.emater.midmipsystem.repository.base;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    
}
