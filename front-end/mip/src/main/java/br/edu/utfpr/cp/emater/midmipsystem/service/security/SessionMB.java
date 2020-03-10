package br.edu.utfpr.cp.emater.midmipsystem.service.security;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.springframework.security.core.context.SecurityContextHolder;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;

@Named
@ViewScoped
public class SessionMB implements Serializable {

    public String getCurrentUser() {
        return ((MIPUserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getFullName();
    }
}