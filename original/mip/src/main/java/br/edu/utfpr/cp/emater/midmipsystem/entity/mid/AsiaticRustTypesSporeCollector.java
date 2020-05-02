package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

public enum AsiaticRustTypesSporeCollector {
    SEM_ESPOROS_FERRUGEM ("Sem Esporos de Ferrugem"),
    COM_ESPOROS_SEM_TESTAR_VIABILIDADE ("Com Esporos - Mas, sem testar viabilidade"),
    COM_ESPOROS_INVIAVEIS_APOS_TESTE ("Com Esporos - Mas, inviáveis após teste"),
    COM_ESPOROS_VIAVEIS_ISOLADOS ("Com Esporos Viáveis - Isolados"),
    COM_ESPOROS_VIAVEIS_AGLOMERADOS ("Com Esporos Viáveis - Aglomerados");

    private String description;

    AsiaticRustTypesSporeCollector(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
