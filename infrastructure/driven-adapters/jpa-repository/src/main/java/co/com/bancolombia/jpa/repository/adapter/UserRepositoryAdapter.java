package co.com.bancolombia.jpa.repository.adapter;

import co.com.bancolombia.jpa.Entity.UserEntity;
import co.com.bancolombia.jpa.repository.JpaUserRepository;
import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User getByUsername(String username) {
        UserEntity entity = jpaUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return new User(entity.getUsername(), entity.getPassword(), entity.isEnabled());
    }

//    public void saveUser(User user) {
//        UserEntity entity = new UserEntity();
//        entity.setUsername(user.getUsername());
//        entity.setPassword(user.getPassword());
//        entity.setEnabled(user.isEnabled());
//        jpaUserRepository.save(entity);
//    }
}