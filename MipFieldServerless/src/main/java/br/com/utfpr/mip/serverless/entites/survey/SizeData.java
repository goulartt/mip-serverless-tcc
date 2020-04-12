package br.com.utfpr.mip.serverless.entites.survey;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeData implements Serializable {
    
    private double totalArea;
    private double totalPlantedArea;
    private double plantPerMeter;
}
