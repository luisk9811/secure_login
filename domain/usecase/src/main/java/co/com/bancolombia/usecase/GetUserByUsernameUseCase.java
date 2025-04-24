package co.com.bancolombia.usecase;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserRepository;
import lombok.RequiredArgsConstructor;

public class GetUserByUsernameUseCase implements UserRepository {

    @Override
    public User getByUsername(String username) {
        if ("admin".equals(username)) {
            return new User(
                    "admin",
                    "$argon2id$v=19$m=19456,t=2,p=1$g+qXm5r4JuzhEtHPi5+eNQ$8fpQVQlmnSxq3pljm/bajnjL94rLAujx9itL2GqLWHc", //hash generado con la contraseña admin123
                    true
            );
        }
        throw new RuntimeException("User not found: " + username);
    }
}


//@RequiredArgsConstructor
//public class GetUserByUsernameUseCase {
//
//    private final UserRepository userRepository;
//
//    public User getByUsername(String username) {
//        return userRepository.getByUsername(username);
//    }
//}