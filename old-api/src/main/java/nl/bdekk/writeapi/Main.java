package nl.bdekk.writeapi;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.logging.LoggingFraction;

import java.util.logging.Level;

public class Main {

    public static void main(String[] args) throws Exception {
        Swarm swarm = new Swarm();

        Level logLevel = Level.INFO;
        swarm.fraction(new LoggingFraction()
                .formatter("PATTERN", "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n"));
//                .consoleHandler(logLevel, "PATTERN")
//                .rootLogger(logLevel, "CONSOLE"));
        swarm.start().deploy();
    }
}