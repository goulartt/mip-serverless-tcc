package br.com.utfpr.mip.serverless.entites.survey;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.utfpr.mip.serverless.entites.base.MIPUser;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Harvest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @EqualsAndHashCode.Include
    @Temporal (TemporalType.DATE)
    private Date begin;

    @EqualsAndHashCode.Include
    @Temporal (TemporalType.DATE)
    private Date end;
    
    @Column(name = "created_at")
    private Long createdAt;
    
    @Column(name = "last_modified")
    private Long lastModified;
    
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private MIPUser createdBy;
    
    @ManyToOne
    @JoinColumn(name = "modified_by_id")
    private MIPUser modifiedBy;
    
    @Builder
    public static Harvest create (Long id, String name, Date begin, Date end) {
        var instance = new Harvest();
        instance.setId(id);
        instance.setName(name);
        instance.setBegin(begin);
        instance.setEnd(end);
        
        return instance;
    }
}
