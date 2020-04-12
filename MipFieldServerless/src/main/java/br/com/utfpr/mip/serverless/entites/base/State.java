package br.com.utfpr.mip.serverless.entites.base;

public enum State {

    PR ("Paraná"),
    SP ("São Paulo"),
    SC ("Santa Catarina"),
    RS ("Rio Grande do Sul");
    
    private String name;
    
    State (String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
}