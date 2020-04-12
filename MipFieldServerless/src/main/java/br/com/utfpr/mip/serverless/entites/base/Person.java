package br.com.utfpr.mip.serverless.entites.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @EqualsAndHashCode.Include
    protected String name;

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

}
