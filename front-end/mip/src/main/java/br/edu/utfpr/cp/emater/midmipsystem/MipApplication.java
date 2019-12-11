package br.edu.utfpr.cp.emater.midmipsystem;

import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUser;
import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import br.edu.utfpr.cp.emater.midmipsystem.service.security.SystemAuditorAware;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableWebSecurity
public class MipApplication {

    public static void main(String[] args) {
        SpringApplication.run(MipApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("pt", "BR"));
        return slr;
    }

    @Bean
    AuditorAware<MIPUser> auditorProvider() {
        return new SystemAuditorAware();
    }
}
