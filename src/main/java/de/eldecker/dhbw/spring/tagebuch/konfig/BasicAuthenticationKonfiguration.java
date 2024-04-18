package de.eldecker.dhbw.spring.tagebuch.konfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Logger LOG = LoggerFactory.getLogger( BasicAuthenticationKonfiguration.class );

    private static final String ROLLE_NUTZER = "nutzer";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf( (csrf) -> csrf.disable() ) // https://stackoverflow.com/a/74753955
                    .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/app")
                                .hasRole(ROLLE_NUTZER)
                                .anyRequest().authenticated() )
                    .httpBasic();

        return httpSecurity.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("alice")
                .password("g3h3im")
                .roles(ROLLE_NUTZER)
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}