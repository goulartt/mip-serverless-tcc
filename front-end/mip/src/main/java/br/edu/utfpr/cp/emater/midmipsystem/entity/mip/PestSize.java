package br.edu.utfpr.cp.emater.midmipsystem.entity.mip;

public enum PestSize {
    
    MAIOR_15CM (">= 1,5 cm"),
    MENOR_15CM ("< 1,5 cm"),
    TERCEIRO_AO_QUINTO_INSTAR ("Ninfa (3. ao 5. instar)"),
    PONTEIROS_ATACADOS ("Ponteiros atacados"),
    ADULTO ("Adulto"),
    OUTRO("Outro");
    
    private String name;
    
    PestSize (String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
}
