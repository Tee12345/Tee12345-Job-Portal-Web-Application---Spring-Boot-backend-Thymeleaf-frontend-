package com.babatunde.config;

import com.babatunde.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;

@Configuration
public class WebSecurityConfig {

    private CustomUserDetailsService customUserDetailsService;
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,
                             CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    private String[] noAuthUrls = {"/",
            "/register",
            "/register/**",
            "/error",
            "/fonts**",
            "/favicon.ico",
            "/resources/**",
            "/global-search/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

            http.authorizeHttpRequests(auth -> {
                auth.requestMatchers(noAuthUrls).permitAll();
                auth.anyRequest().authenticated();
            });

            http.formLogin(form -> form.loginPage("/login").permitAll()
                    .successHandler(customAuthenticationSuccessHandler))
                    .logout(logout-> {
                        logout.logoutUrl("/logout");
                        logout.logoutSuccessUrl("/");
                    }).cors(Customizer.withDefaults())
                    .csrf(csrf -> csrf.disable());;

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
    }
}
