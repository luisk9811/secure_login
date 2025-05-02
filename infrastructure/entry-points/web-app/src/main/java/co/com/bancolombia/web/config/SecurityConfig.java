package co.com.bancolombia.web.config;

import co.com.bancolombia.usecase.GetUserByUsernameUseCase;
import co.com.bancolombia.web.security.CaptchaValidationFilter;
import co.com.bancolombia.web.security.CustomAuthenticationProvider;
import co.com.bancolombia.web.security.UserDetailsServiceImpl;
import co.com.bancolombia.web.security.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${reCAPTCHA.url}")
    private String recaptchaUrl;
    @Value("${reCAPTCHA.secret-key}")
    private String recaptchaSecretKey;

    private static final int ARGON2_MEMORY_MIB = 19 * 1024;
    private static final int ARGON2_ITERATIONS = 2;
    private static final int ARGON2_PARALLELISM = 1;
    private static final int ARGON2_SALT_LENGTH = 16;
    private static final int ARGON2_HASH_LENGTH = 32;

    private final GetUserByUsernameUseCase getUserByUsernameUseCase;
    private final UserDetailsServiceImpl userDetailsService;
    private final LoginAttemptService loginAttemptService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
                ARGON2_SALT_LENGTH,
                ARGON2_HASH_LENGTH,
                ARGON2_PARALLELISM,
                ARGON2_MEMORY_MIB,
                ARGON2_ITERATIONS
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CaptchaValidationFilter captchaValidationFilter) throws Exception {
        http
//                .csrf(csrf -> csrf.disable())
                .addFilterBefore(captchaValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public CaptchaValidationFilter captchaValidationFilter() {
        return new CaptchaValidationFilter(recaptchaUrl, recaptchaSecretKey);
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl(getUserByUsernameUseCase);
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider provider = new CustomAuthenticationProvider(loginAttemptService);
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider())
                .build();
    }
}