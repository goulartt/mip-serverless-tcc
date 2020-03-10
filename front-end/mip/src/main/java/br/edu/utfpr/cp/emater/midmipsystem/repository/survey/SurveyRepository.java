package br.edu.utfpr.cp.emater.midmipsystem.repository.survey;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;


public interface SurveyRepository extends JpaRepository<Survey, Long>{ }
