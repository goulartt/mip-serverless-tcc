package br.edu.utfpr.cp.emater.midmipsystem.entity.survey;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CultivarData implements Serializable {

    @Size(min = 3, max = 50, message = "A identificação da cultivar deve ter entre 3 e 50 caracteres")
    private String name;

    private boolean rustResistant;
    private boolean bt;

    public void setSeedName(String seedName) {
        this.name = WordUtils.capitalize(name.toLowerCase());
    }
}
