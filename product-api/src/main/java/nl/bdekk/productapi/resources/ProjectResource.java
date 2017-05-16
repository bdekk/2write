package nl.bdekk.productapi.resources;

import nl.bdekk.productapi.domain.Error;
import nl.bdekk.productapi.domain.Project;
import nl.bdekk.productapi.services.ProjectService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
@Path("/project")
public class ProjectResource {

    @Inject
    private ProjectService projectService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
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

    @GET
    @Path("{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProject(@PathParam("projectId") long projectId) {
        Project project = projectService.getProject(projectId);

        if(project == null) {
            return  Response.status(404).build();
        }
        return Response.ok(project).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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