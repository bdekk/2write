package nl.bdekk.authapi.dao;

import nl.bdekk.authapi.domain.User;
import nl.bdekk.authapi.domain.entity.UserEntity;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.text.html.Option;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

@ApplicationScoped
public class UserDao {


    @Inject
    EntityManager manager;

    public Optional<User> login(String username, String password) {
        TypedQuery<UserEntity> query = manager.createNamedQuery(UserEntity.FIND_BY_USERNAME_PASSWORD, UserEntity.class);
        query = query.setParameter("username", username);
        query = query.setParameter("password", hashPassword(password.toCharArray()));
        Optional<UserEntity> optional = Optional.ofNullable(query.getSingleResult());

        Optional<User> userOptional = Optional.empty();
        if(optional.isPresent()) {
            userOptional = Optional.of(userEntityToUser(optional.get()));
        }
        return userOptional;
    }

    public User createUser(User user) {
        UserEntity entity = this.userToUserEntity(user);
        manager.getTransaction().begin();
        manager.persist(entity);
        manager.getTransaction().commit();
        user.setPassword(null);
        return user;
    }

    private UserEntity userToUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setUsername(user.getUsername());

        String userPassword = user.getPassword();
        byte[] hashedPassword = hashPassword(userPassword.toCharArray());

        entity.setPassword(hashedPassword);
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        return entity;
    }

    private User userEntityToUser(UserEntity entity) {
        User user = new User();
        user.setUsername(entity.getUserName());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        return user;
    }

    public static byte[] hashPassword( final char[] password) {

        final int SALT_BYTES = 24;
        final int ITERATIONS = 1000;
        final int KEY_LENGTH = 10;

        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, ITERATIONS, KEY_LENGTH );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;

        } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
}
