package nl.bdekk.writeapi.writeapi.rest.application;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("/projects")
public class JaxRsApplication extends Application {
}