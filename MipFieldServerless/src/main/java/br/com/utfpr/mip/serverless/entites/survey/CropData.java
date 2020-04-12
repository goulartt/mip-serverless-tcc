package br.com.utfpr.mip.serverless.entites.survey;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CropData implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date sowedDate;

    @Temporal(TemporalType.DATE)
    private Date emergenceDate;

    @Temporal(TemporalType.DATE)
    private Date harvestDate;
}
