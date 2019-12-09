package br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation;

public enum ToxiClass {

    
    I("I"),
    II("II"),
    III("III"),
    IV("IV"),
    V("V"),
    INDEFINIDA("INDEFINIDA");

    private String description;

    ToxiClass(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
