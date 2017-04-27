package nl.bdekk.writeapi.resources;

import nl.bdekk.writeapi.domain.Error;
import nl.bdekk.writeapi.domain.Project;
import nl.bdekk.writeapi.services.ProjectService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
@Path("/")
public class ProjectResource {

    @Inject
    private ProjectService projectService;

    @GET
    @Produces("application/json")
    public Response getProjects() {
        List<Project> projects = null;
        try {
            projects = projectService.getProjects();
        } catch (IOException e) {
            Error err = new Error();
            err.setMessage(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }
        return Response.ok(projects).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createProject(final Project input) {
        Project project = null;

        if(input == null) {
            return Response.status(422).build();
        }

        try {
            project = projectService.createProject(input.getTitle(), input.getDescription());
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