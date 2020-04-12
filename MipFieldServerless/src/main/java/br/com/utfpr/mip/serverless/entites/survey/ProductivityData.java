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
public class ProductivityData implements Serializable {
    
    private double productivityField;
    private double productivityFarmer;
    private boolean separatedWeight;
}
