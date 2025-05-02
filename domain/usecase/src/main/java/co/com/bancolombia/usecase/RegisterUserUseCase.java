package co.com.bancolombia.usecase;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    public void register(User user) {
        userRepository.saveUser(user);
    }
}
