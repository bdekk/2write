package nl.bdekk.authapi.services;

import nl.bdekk.authapi.dao.UserDao;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TokenService {

    @Inject
    private UserDao dao;
}
