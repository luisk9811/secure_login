package co.com.bancolombia.web.security;

import co.com.bancolombia.web.security.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private final LoginAttemptService loginAttemptService;
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class); // Logging

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        if (loginAttemptService.isBlocked(username)) {
            logger.warn("Intento de login para usuario bloqueado: {}", username);
            throw new LockedException("La cuenta está temporalmente bloqueada.");
        }

        try {
            Authentication auth = super.authenticate(authentication);

            logger.info("Login exitoso para usuario: {}", username);
            loginAttemptService.loginSucceeded(username);
            return auth;

        } catch (BadCredentialsException e) {
            logger.warn("Credenciales incorrectas para usuario: {}", username);
            loginAttemptService.loginFailed(username);
            throw e;

        } catch (AuthenticationException e) {
            logger.warn("Fallo de autenticación para usuario: {} - Causa: {}", username, e.getMessage());
            loginAttemptService.loginFailed(username);
            throw e;
        }
    }
}