package br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation;

public enum ProductUnit {

    
    L("L"),
    ML("ML"),
    KG("KG"),
    G("G");

    private String description;

    ProductUnit(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
