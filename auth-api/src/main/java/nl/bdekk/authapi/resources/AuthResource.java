package nl.bdekk.authapi.resources;

import nl.bdekk.authapi.domain.Token;
import nl.bdekk.authapi.domain.User;
import nl.bdekk.authapi.services.TokenService;
import nl.bdekk.authapi.services.UserService;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

@ApplicationScoped
@Path("/auth")
public class AuthResource {

    @Inject
    private TokenService tokenService;

    @Inject
    private UserService userService;

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getToken(@HeaderParam("Authorization") String authorization) {
        Response response;
        if (authorization != null && authorization.startsWith("Basic")) {
            String[] usernamePassword = decodeBasicAuthorizationHeader(authorization);
            Optional<User> user = userService.validLogin(usernamePassword[0], usernamePassword[1]);
            if(user.isPresent()) {
                Token token = tokenService.createAndSignToken(user.get());
                response = Response.ok().entity(token).build();
            } else {
                response = Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    private String[] decodeBasicAuthorizationHeader(String authorization) {
        // Authorization: Basic base64credentials
        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));
        // credentials = username:password
        final String[] values = credentials.split(":", 2);
        return values;
    }

}
