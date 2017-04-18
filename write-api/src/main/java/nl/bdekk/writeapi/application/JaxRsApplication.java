package nl.bdekk.writeapi.application;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("/project")
public class JaxRsApplication extends Application {
}