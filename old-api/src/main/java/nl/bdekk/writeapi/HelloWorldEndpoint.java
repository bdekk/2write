package nl.bdekk.writeapi;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/documents")
public class HelloWorldEndpoint {

	private static Logger LOG = Logger.getLogger(HelloWorldEndpoint.class.getName());

	@GET
	@Produces("text/plain")
	public Response doGet() {
		return Response.ok("Hello from WildFly Swarm!").build();
	}
}