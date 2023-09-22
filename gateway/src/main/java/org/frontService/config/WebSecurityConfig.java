package org.frontService.config;

import lombok.RequiredArgsConstructor;
import org.frontService.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AccountService accountService;
    private final DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/homePage")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/registration")).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/homePage")
                        .permitAll());
        return http.build();
    }

    @Bean
    UserDetailsManager users(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(accountService)
                .passwordEncoder(passwordEncoder())
                .and()
                .authenticationProvider(authenticationProvider())
                .build();

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setAuthenticationManager(authenticationManager);
        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
