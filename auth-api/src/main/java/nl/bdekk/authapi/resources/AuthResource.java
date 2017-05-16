package nl.bdekk.authapi.resources;

import nl.bdekk.authapi.services.TokenService;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

@ApplicationScoped
@Path("")
public class AuthResource {

    @Inject
    private TokenService tokenService;

}
