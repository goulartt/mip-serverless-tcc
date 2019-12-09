package br.edu.utfpr.cp.emater.midmipsystem.entity.mip;

public enum PestSize {
    
    MAIOR_15CM ("> 15 cm"),
    MENOR_15CM ("< 15 cm"),
    TERCEIRO_AO_QUINTO_INSTAR ("3. ao 5. Instar"),
    ADULTOS ("Adultos");
    
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
