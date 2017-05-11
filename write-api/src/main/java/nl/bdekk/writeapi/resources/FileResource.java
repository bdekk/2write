package nl.bdekk.writeapi.resources;

import nl.bdekk.writeapi.domain.Project;
import nl.bdekk.writeapi.domain.ProjectFile;
import nl.bdekk.writeapi.services.FileService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/file")
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
        return response.build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFile(@PathParam("fileId") long fileId) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
