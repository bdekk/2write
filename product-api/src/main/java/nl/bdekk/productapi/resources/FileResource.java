package nl.bdekk.productapi.resources;

import nl.bdekk.productapi.domain.Error;
import nl.bdekk.productapi.domain.ProjectFile;
import nl.bdekk.productapi.services.FileService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/project/{projectId}/file")
public class FileResource {

    @Inject
    private FileService fileService;

    @GET
    @Path("{fileId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("fileId") long fileId) {
        String file = fileService.getFile(fileId);
        if(file == null) {
            return  Response.status(404).build();
        }

        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + fileId + "\"" ) //optional
                .build();

    }

    @PUT
    @Path("{fileId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFile(@PathParam("fileId") long fileId, final ProjectFile input) {
        boolean success = fileService.updateFile(fileId, input);
        Response.ResponseBuilder response = Response.ok();
        if(!success) {
            response = Response.status(Response.Status.BAD_REQUEST);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("success", success);
        } catch (JSONException e) {
            Error err = new Error();
            err.setMessage(e.getMessage());
            response = Response.status(Response.Status.BAD_REQUEST).entity(err);
        }

        return response.entity(json).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFile(@PathParam("projectId") long projectId, final ProjectFile input) {
        ProjectFile file = null;

        if(input == null) {
            return Response.status(422).build();
        }

        try {
            file = fileService.createFile(projectId, input.getName());
        } catch (Exception e) {
            Error err = new Error();
            err.setMessage(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        return Response
                .status(Response.Status.CREATED)
                .entity(file)
                .build();
    }
}
