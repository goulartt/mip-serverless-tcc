package br.edu.utfpr.cp.emater.midmipsystem.repository.pulverisation;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Product;

public interface ProductRepository extends JpaRepository<Product, Long> { }
