package br.com.utfpr.mip.serverless.entites.survey;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PulverisationData implements Serializable {

    private double soyaPrice;

    private double applicationCostCurrency;

    @Builder
    public static PulverisationData create(double soyaPrice, double applicationCostCurrency) {
        var instance = new PulverisationData();
        instance.setSoyaPrice(soyaPrice);
        instance.setApplicationCostCurrency(applicationCostCurrency);

        return instance;
    }
  
}
