package br.edu.utfpr.cp.emater.midmipsystem.entity.pulverisation;

public enum UseClass {

    ACARICIDA("Acaricida"),
    BACTERICIDA("Bactericida"),
    ESPALHANTE_ADESIVO("Espalhante Adesivo"),
    FUNGICIDA("Fungicida"),
    ESTIMULANTE("Estimulante"),
    HERBICIDA("Herbicida"),
    INSETICIDA("Inseticida"),
    OUTROS("Outros"),
    ADJUVANTE("Adjuvante"),
    LESMICIDA_MOLUSCICIDA("Lesmicida/Moluscicida"),
    FEROMONIO("Feromônio"),
    NEMATICIDA("Nematicida"),
    FORMICIDA("Formicida"),
    REGULADOR_CRESCIMENTO("Regulador de Crescimento"),
    AGENTE_BIOLOGICO("Agente Biológico"),
    INCETICIDA_BIOLOGICO("Inseticida Biológico"),
    ADUBO_FOLIAR("Adubo Foliar"),
    SAL_COMUM("Sal Comum"),
    MULTIPLAS("Multiplas");

    private String description;

    UseClass(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
