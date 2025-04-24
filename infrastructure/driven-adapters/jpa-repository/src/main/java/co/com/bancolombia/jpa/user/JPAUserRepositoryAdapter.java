//package co.com.bancolombia.jpa.user;
//
//import co.com.bancolombia.model.User;
//import co.com.bancolombia.model.gateways.UserRepository;
//import co.com.bancolombia.jpa.helper.AdapterOperations;
//import org.reactivecommons.utils.ObjectMapper;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class JPAUserRepositoryAdapter extends AdapterOperations<User, UserData, String, JPAUserRepository>
//        implements UserRepository {
//
//    public JPAUserRepositoryAdapter(JPAUserRepository repository, ObjectMapper mapper) {
//        super(repository, mapper, d -> mapper.map(d, User.class));
//    }
//
//    @Override
//    public User getByUsername(String username) {
//        return toEntity(repository.findById(username).orElse(null));
//    }
//}
