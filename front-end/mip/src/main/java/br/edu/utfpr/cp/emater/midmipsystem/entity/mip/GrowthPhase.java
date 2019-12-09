package br.edu.utfpr.cp.emater.midmipsystem.entity.mip;

public enum GrowthPhase {

    
    VE("VE"),
    VC("VC"),
    V1("V1"),
    V2("V2"),
    V3("V3"),
    V4("V4"),
    V5("V5"),
    V6("V6"),
    V7("V7"),
    V8("V8"),
    V9("V9"),
    V10("V10"),
    V11("V11"),
    V12("V12"),
    V13("V13"),
    V14("V14"),
    V15("V15"),
    V16("V16"),
    V17("V17"),
    V18("V18"),
    VN("VN"),
    R1("R1"),
    R2("R2"),
    R3("R3"),
    R4("R4"),
    R5("R5"),
    R5_1("R5.1"),
    R5_2("R5.2"),
    R5_3("R5.3"),
    R5_4("R5.4"),
    R5_5("R5.5"),
    R5_6("R5.6"),
    R6("R6"),
    R6_1("R6.1"),
    R6_2("R6.2"),
    R6_3("R6.3"),
    R6_4("R6.4"),
    R6_5("R6.5"),
    R6_R7("R6/R7"),
    R7("R7"),
    R7_1("R7.1"),
    R7_2("R7.2"),
    R7_3("R7.3"),
    R8("R8"),
    R8_1("R8.1"),
    R8_2("R8.2"),
    R9("R9"),
    R5_5_R6("R5.5/R6"),
    R4_R5("R4/R5");

    private String description;

    GrowthPhase(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
