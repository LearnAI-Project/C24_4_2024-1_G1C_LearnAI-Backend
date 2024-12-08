package pe.edu.tecsup.learnai.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.tecsup.learnai.security.JWT.JwtAuthenticationFilter;
import pe.edu.tecsup.learnai.security.JWT.JwtUtils;
import pe.edu.tecsup.learnai.services.oauth2.OAuth2LoginSuccessHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private JwtUtils jwtUtils;

    @Autowired
    public WebSecurityConfig(@Lazy OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler, JwtUtils jwtUtils) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers("/public/**", "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/auth/verify").permitAll()
                        .requestMatchers("/", "/error", "/csrf", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // Añadir el filtro JWT
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, customUserDetailsService);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider(passwordEncoder()))
                .build();
    }

}