package nl.bdekk.writeapi.writeapi.rest.rest;

import nl.bdekk.writeapi.writeapi.rest.dto.Project;
import nl.bdekk.writeapi.writeapi.rest.services.ProjectService;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

@ApplicationScoped
@Path("/")
public class Projects {

	@Inject
	private ProjectService projectService;

	@GET
	@Produces("text/plain")
	public Response getProjects() {


		return Response.ok("Hello from WildFly Swarm!").build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createProject() {
		Project project = null;
		try {
			project = projectService.createProject("project 1", "b");
		} catch (IOException | GitAPIException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		return Response
				.ok(project)
				.status(Response.Status.CREATED)
				.build();
	}
}