package br.edu.utfpr.cp.emater.midmipsystem.entity.survey;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MIDData implements Serializable {

    private boolean sporeCollectorPresent;
}
