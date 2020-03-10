package br.edu.utfpr.cp.emater.midmipsystem.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;

public interface FieldRepository extends JpaRepository<Field, Long>{ }
