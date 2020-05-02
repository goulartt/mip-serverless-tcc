package br.edu.utfpr.cp.emater.midmipsystem.view.pulverisation;

import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Product;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.ProductUnit;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.Target;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.ToxiClass;
import br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation.UseClass;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.pulverisation.ProductService;
import br.edu.utfpr.cp.emater.midmipsystem.view.ICRUDController;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
public class ProductController extends Product implements ICRUDController<Product> {

    private final ProductService productService;

    @Getter
    @Setter
    private Long targetId;

    @Override
    public List<Product> readAll() {
        return productService.readAll();
    }

    public ProductUnit[] readAllUnits() {
        return ProductUnit.values();
    }

    public UseClass[] readAllUseClasses() {
        return UseClass.values();
    }
    
    public ToxiClass[] readAllToxiClasses(){
        return ToxiClass.values();
    }

    @Override
    public String create() {

        try {
            var newProduct = Product.builder()
                    .name(this.getName())
                    .unit(this.getUnit())
                    .useClass(this.getUseClass())
                    .activeIngredient(this.getActiveIngredient())
                    .company(this.getCompany())
                    .concentrationActiveIngredient(this.getConcentrationActiveIngredient())
                    .registerNumber(this.getRegisterNumber())
                    .toxiClass(this.getToxiClass())
                    .build();

            productService.create(newProduct);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", String.format("Produto [%s] criado com sucesso!", newProduct.getName())));
            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um produto com esse nome! Use um nome diferente."));
            return "create.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Produto não pode ser criado porque o alvo/função não foi encontrado na base de dados!"));
            return "create.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String prepareUpdate(Long anId) {

        try {
            var existentProduct = productService.readById(anId);

            this.setId(existentProduct.getId());
            this.setName(existentProduct.getName());
            this.setUnit(existentProduct.getUnit());
            this.setUseClass(existentProduct.getUseClass());
            this.setActiveIngredient(existentProduct.getActiveIngredient());
            this.setCompany(existentProduct.getCompany());
            this.setConcentrationActiveIngredient(existentProduct.getConcentrationActiveIngredient());
            this.setRegisterNumber(existentProduct.getRegisterNumber());
            this.setToxiClass(existentProduct.getToxiClass());

            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Produto não pode ser alterado porque não foi encontrado na base de dados!"));
            return "index.xhtml";
        }
    }

    @Override
    public String update() {

        try {
            var updatedProduct = Product.builder()
                                        .id(this.getId())
                                        .name(this.getName())
                                        .unit(this.getUnit())
                                        .useClass(this.getUseClass())
                                        .activeIngredient(this.getActiveIngredient())
                                        .company(this.getCompany())
                                        .concentrationActiveIngredient(this.getConcentrationActiveIngredient())
                                        .registerNumber(this.getRegisterNumber())
                                        .toxiClass(this.getToxiClass())
                                     .build();

            productService.update(updatedProduct);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Produto alterado!"));

            return "index.xhtml";

        } catch (EntityAlreadyExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um produto com esse nome! Use um nome diferente."));
            return "update.xhtml";

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Produto não pode ser alterado porque não foi encontrada na base de dados!"));
            return "update.xhtml";

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
            return "index.xhtml";
        }

    }

    public String delete(Long anId) {

        try {
            productService.delete(anId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Produto excluído!"));

        } catch (EntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Produto não pode ser excluído porque não foi encontrado na base de dados!"));

        } catch (EntityInUseException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Produto não pode ser excluído porque está sendo usado no sistema!"));

        } catch (AnyPersistenceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro na gravação dos dados!"));
        }

        return "index.xhtml";
    }

    public List<Target> readAllTargets() {
        return productService.readAllTargets();
    }
}
