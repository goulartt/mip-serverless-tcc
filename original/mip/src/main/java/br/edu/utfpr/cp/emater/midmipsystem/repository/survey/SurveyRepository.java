package br.edu.utfpr.cp.emater.midmipsystem.repository.survey;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SurveyRepository extends JpaRepository<Survey, Long>{ }
