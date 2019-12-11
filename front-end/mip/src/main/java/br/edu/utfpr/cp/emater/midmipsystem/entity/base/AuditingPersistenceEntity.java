package br.edu.utfpr.cp.emater.midmipsystem.entity.base;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUser;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreRemove;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;

@Data 
@MappedSuperclass
@EntityListeners (AuditingEntityListener.class)
@Log
public abstract class AuditingPersistenceEntity implements Serializable {

    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long lastModified;
    
    @CreatedBy
    @ManyToOne
    private MIPUser createdBy;
    
    @LastModifiedBy
    @ManyToOne
    private MIPUser modifiedBy;
    
    @PreRemove
    protected void onPreRemove() {
        
        var currentUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        
        var message = String
                .format("[%s] tried to remove entity [%s].", 
                        currentUser.getUsername(),
                        this.getClass().getCanonicalName());
        
        log.warning(message);
    }
}
