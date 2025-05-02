package co.com.bancolombia.web.security;

import co.com.bancolombia.usecase.GetUserByUsernameUseCase;
import co.com.bancolombia.web.security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final GetUserByUsernameUseCase getUserByUsernameUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            return new UserPrincipal(
                    getUserByUsernameUseCase.getByUsername(username)
            );
        } catch (RuntimeException e) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username, e);
        }
    }
}