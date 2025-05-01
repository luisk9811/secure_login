package co.com.bancolombia.usecase;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetUserByUsernameUseCase {

    private final UserRepository userRepository;

    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
