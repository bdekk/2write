package nl.bdekk.productapi.application;

import org.wildfly.swarm.Swarm;

public class Main {

    public static void main(String[] args) throws Exception {
        Swarm swarm = new Swarm();
        swarm.withProperty("swarm.context.path" , ApplicationProperties.getContextPath());
        swarm.withProperty("swarm.http.port" , Integer.toString(ApplicationProperties.getHTTPPort()));
        swarm.start();
        swarm.deploy();
    }
}
