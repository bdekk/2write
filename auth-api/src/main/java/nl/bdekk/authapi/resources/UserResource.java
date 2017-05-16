package nl.bdekk.authapi.resources;

import nl.bdekk.authapi.domain.ErrorObj;
import nl.bdekk.authapi.domain.User;
import nl.bdekk.authapi.services.UserService;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@ApplicationScoped
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User input) {
        Response response;
        if(input == null) {
            response = Response.status(Response.Status.BAD_REQUEST).build();
            return response;
        }

        User user = userService.create(input);
        if(user == null) {
            ErrorObj error = new ErrorObj();
            error.setMessage("Could not create user.");
            response = Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } else {
            response = Response.status(Response.Status.CREATED).build();
        }
        return response;
    }

}
