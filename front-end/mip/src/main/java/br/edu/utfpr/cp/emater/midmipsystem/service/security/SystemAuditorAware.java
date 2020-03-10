package br.edu.utfpr.cp.emater.midmipsystem.service.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUser;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;

public class SystemAuditorAware implements AuditorAware<MIPUser> {
    
  public Optional<MIPUser> getCurrentAuditor() {

     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

      return Optional.of(((MIPUserPrincipal) authentication.getPrincipal()).getUser());
  }
}
