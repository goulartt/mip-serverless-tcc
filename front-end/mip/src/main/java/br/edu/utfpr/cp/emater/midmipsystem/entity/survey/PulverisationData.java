package br.edu.utfpr.cp.emater.midmipsystem.entity.survey;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PulverisationData implements Serializable {

    @Positive(message = "O preço da soja precisa ser um valor maior que zero.")
    private double soyaPrice;

    @Positive(message = "O custo da aplicação precisa ser um valor maior que zero.")
    private double applicationCostCurrency;

    @Builder
    public static PulverisationData create(double soyaPrice, double applicationCostCurrency) {
        var instance = new PulverisationData();
        instance.setSoyaPrice(soyaPrice);
        instance.setApplicationCostCurrency(applicationCostCurrency);

        return instance;
    }
    
    @JsonIgnore
    public double getApplicationCostQty() {
         if (soyaPrice != 0) 
              if (applicationCostCurrency != 0) 
                    return (this.getApplicationCostCurrency() / this.getSoyaPrice());
         
         return 0;
    }
}
