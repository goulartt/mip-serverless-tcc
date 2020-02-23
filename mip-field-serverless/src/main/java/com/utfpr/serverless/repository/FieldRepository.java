package com.utfpr.serverless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utfpr.serverless.entities.Field;


public interface FieldRepository extends JpaRepository<Field, Long>{ }
