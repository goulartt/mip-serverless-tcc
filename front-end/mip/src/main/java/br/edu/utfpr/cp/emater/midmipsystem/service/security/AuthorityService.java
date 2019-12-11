package br.edu.utfpr.cp.emater.midmipsystem.service.security;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.Authority;
import br.edu.utfpr.cp.emater.midmipsystem.repository.security.AuthorityRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    
    private final AuthorityRepository authorityRepository;

    public Authority readByName(String aName) {
        return authorityRepository.findByName(aName);
    }
    
    public List<Authority> readAll() {
        return List.copyOf(authorityRepository.findAll());
    }

    public Optional<Authority> readById(Long id) {
        return authorityRepository.findById(id);
    }

}
