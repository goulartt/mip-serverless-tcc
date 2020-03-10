package br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PulverisationOperationOccurrence implements Serializable {

    @ManyToOne
    @NotNull(message = "Um produto deve ser informado")
    private Product product;

    @Positive(message = "O preço do produto deve ser informado")
    private double productPrice;

    @ManyToOne
    private Target target;

    @Positive(message = "A dosagem do produto deve ser informada")
    private double dose;

    @Builder
    private static PulverisationOperationOccurrence create(Product product, double productPrice, Target target, double dose) {
        var instance = new PulverisationOperationOccurrence();

        instance.setProduct(product);
        instance.setTarget(target);
        instance.setDose(dose);
        instance.setProductPrice(productPrice);

        return instance;
    }

    public double getProductCostCurrency() {
        if (this.getProductPrice() != 0) 
            if (this.getDose() != 0)
                return this.getProductPrice() * this.getDose();
        
        return 0;
    }
    
    public double getProductCostQty() {
        return 0;
    }

    public String getTargetCategoryDescription() {
        if (this.getTarget() != null)
            return this.getTarget().getUseClass().getDescription();
        
        return null;
    }

    public String getTargetDescription() {
        if (this.getTarget() != null)
            return this.getTarget().getDescription();
        
        return null;
    }

    public String getProductFormattedName() {
        if (this.getProduct() != null)
            return String.format("%s - %.2f (%s)", this.getProduct().getName(), this.getDose(), this.getProduct().getUnit().getDescription());
        
        return null;
    }
}
