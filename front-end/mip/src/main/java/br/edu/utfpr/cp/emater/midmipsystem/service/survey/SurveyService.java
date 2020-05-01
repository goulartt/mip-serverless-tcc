package br.edu.utfpr.cp.emater.midmipsystem.service.survey;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.survey.Harvest;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final HarvestService harvestService;
    private final CultivarService cultivarService;
   
    public Harvest readHarvestById(Long id) throws EntityNotFoundException {
        return harvestService.readById(id);
    }
    
    public List<Harvest> readAllHarvests() {
        return harvestService.readAll();
    }

    public List<String> searchCultivar(String excerpt) {
        return cultivarService.readByExcerptName(excerpt);
    }
}
