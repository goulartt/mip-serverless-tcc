package br.edu.utfpr.cp.emater.midmipsystem.service.analysis;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DAEAndOccurrenceDTO {
    
    private int dae;
    private double occurrence;
    
    @Builder
    public static DAEAndOccurrenceDTO create (int dae, double occurrence) {
        var result = new DAEAndOccurrenceDTO();
        
        result.dae = dae;
        result.occurrence = occurrence;
        
        return result;
    }
    
}
