package br.edu.utfpr.cp.emater.midmipsystem.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;

public interface CityRepository extends JpaRepository<City, Long> { }
