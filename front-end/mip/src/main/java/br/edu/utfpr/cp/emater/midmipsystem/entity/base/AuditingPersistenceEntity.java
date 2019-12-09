package br.edu.utfpr.cp.emater.midmipsystem.entity.base;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data 
@MappedSuperclass
@EntityListeners (AuditingEntityListener.class)
public class AuditingPersistenceEntity {

    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long lastModified;
}
