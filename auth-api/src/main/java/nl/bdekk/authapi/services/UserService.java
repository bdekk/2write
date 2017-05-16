package nl.bdekk.authapi.services;

import nl.bdekk.authapi.dao.UserDao;
import nl.bdekk.authapi.domain.User;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserDao dao;


    public Optional<User> validLogin(String username, String password) {
        return dao.login(username, password);
    }

    public User create(User user) {
        return dao.createUser(user);
    }
}
