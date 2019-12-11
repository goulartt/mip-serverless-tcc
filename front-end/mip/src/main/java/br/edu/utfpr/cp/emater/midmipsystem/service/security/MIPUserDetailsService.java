package br.edu.utfpr.cp.emater.midmipsystem.service.security;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.edu.utfpr.cp.emater.midmipsystem.repository.security.MIPUserRepository;

@Service
@RequiredArgsConstructor
public class MIPUserDetailsService implements UserDetailsService {
    
    private final MIPUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        var user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        
        return new MIPUserPrincipal(user);
    }
}
