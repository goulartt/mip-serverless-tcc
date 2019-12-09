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
    @NotNull (message = "Um produto deve ser informado")
    private Product product;
    
    @Positive (message = "O preço do produto deve ser informado")
    private double productPrice;
    
    private double productCostCurrency;
    
    private double productCostQty;
    
    @ManyToOne
    private Target target;
    
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
        
        if (this.product == null)
            throw new RuntimeException("Um produto deve ser informado");
        
        this.setProductCostCurrency(this.calculateProductCost());
    }
    
    private double calculateProductCost() {
        return this.getProductPrice() * this.getProduct().getDose();
    }
        
    @Builder
    private static PulverisationOperationOccurrence create(Product product, double productPrice, Target target){
        var instance = new PulverisationOperationOccurrence();
        
        instance.setProduct(product);
        instance.setProductPrice(productPrice);
        instance.setTarget(target);
        
        return instance;
    }
    
    public String getTargetCategoryDescription() {
        return this.getTarget().getCategory().getDescription();
    }
    
    public String getTargetDescription() {
        return this.getTarget().getDescription();
    }
    
    public String getProductFormattedName() {
        return String.format("%s - %.2f (%s)", this.getProduct().getName(), this.getProduct().getDose(), this.getProduct().getUnit().getDescription());
    }
    
}
