package nl.bdekk.writeapi.writeapi.rest.rest;

import nl.bdekk.writeapi.writeapi.rest.dto.Project;
import nl.bdekk.writeapi.writeapi.rest.services.ProjectService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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

	@GET
	@Path("/create")
	@Produces("application/json")
	public Response createProject() {

		Project project = projectService.createProject("bookTwo");
		return Response.ok(project).build();
	}
}