package nl.bdekk.writeapi.resources;

import nl.bdekk.writeapi.dto.Error;
import nl.bdekk.writeapi.dto.Project;
import nl.bdekk.writeapi.services.ProjectService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/")
public class ProjectResource {

    @Inject
    private ProjectService projectService;

    @GET
    @Produces("application/json")
    public Response getProjects() {
        List<Project> projects = projectService.getProjects();
        return Response.ok(projects).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createProject() {
        Project project = null;
        try {
            project = projectService.createProject("project 1", "Description");
        } catch (Exception e) {
            Error err = new Error();
            err.setMessage(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        return Response
                .status(Response.Status.CREATED)
                .entity(project)
                .build();
    }
}