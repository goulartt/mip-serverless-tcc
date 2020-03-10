package br.edu.utfpr.cp.emater.midmipsystem.entity.mid;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.GrowthPhase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MIDSampleLeafInspectionOccurrence implements Serializable{
    
    @Enumerated (EnumType.STRING)
    private GrowthPhase growthPhase;
    
    @Enumerated (EnumType.STRING)
    private AsiaticRustTypesLeafInspection bladeReadingRustResultLeafInspection;
}
