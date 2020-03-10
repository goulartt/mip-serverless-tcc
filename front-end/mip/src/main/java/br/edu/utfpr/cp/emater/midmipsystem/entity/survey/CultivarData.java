package br.edu.utfpr.cp.emater.midmipsystem.entity.survey;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.apache.commons.text.WordUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CultivarData implements Serializable {

    @Size(min = 3, max = 50, message = "A identificação da cultivar deve ter entre 3 e 50 caracteres")
    private String cultivarName;

    private boolean rustResistant;
    private boolean bt;

    public void setCultivarName(String cultivarName) {
        this.cultivarName = WordUtils.capitalize(cultivarName.toLowerCase());
    }
}
