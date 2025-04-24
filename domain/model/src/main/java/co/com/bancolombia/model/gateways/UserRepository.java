package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.User;

public interface UserRepository {
    User getByUsername(String username);
}
