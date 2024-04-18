package de.eldecker.dhbw.spring.tagebuch.konfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class BasicAuthenticationKonfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                    .authorizeHttpRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic();

        return httpSecurity.build();
    }
    
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("alice")
                .password("g3h3im")
                .roles("NUTZER")
                .build();
        
        return new InMemoryUserDetailsManager(user);
    }    

}