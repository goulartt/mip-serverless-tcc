package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

public enum AsiaticRustTypesLeafInspection {
    SEM_LESOES_VISIVEIS ("Sem Lesões Visíveis"),
    COM_LESOES_VISIVEIS_PLANTAS_ISOLADAS ("Com Lesões Visíveis - em Plantas Isoladas"),
    COM_LESOES_VISIVEIS_PARTE_LAVOURA ("Com Lesões Visíveis - em Parte da Lavoura"),
    COM_LESOES_VISIVEIS_TODA_LAVOURA ("Com Lesões Visíveis - em Toda a Lavoura"),
    SEM_FERRUGEM_COM_SINAIS_OUTRAS_DOENCAS ("Sem Ferrugem, mas c/ sinais de Outras Doenças");

    private String description;

    AsiaticRustTypesLeafInspection(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
