package nl.bdekk.writeapi.database;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class PersistenceHelper {

    private final EntityManagerFactory emf;

    public PersistenceHelper() {
        Map<String, Object> dbEnvVariablesMap = getDatabaseEnvVariables();
        emf = Persistence.createEntityManagerFactory("write", dbEnvVariablesMap);
    }

    private Map<String, Object> getDatabaseEnvVariables() {
        Map<String, String> env = System.getenv();
        Map<String, Object> databaseVariablesMap = new HashMap<>();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            if ("DATABASE_URL".equalsIgnoreCase(entry.getKey())) {
                databaseVariablesMap.put("javax.persistence.jdbc.url", entry.getValue());
            }

            if("DATABASE_USERNAME".equalsIgnoreCase(entry.getKey())) {
                databaseVariablesMap.put("javax.persistence.jdbc.user", entry.getValue());

            }

            if("DATABASE_PASSWORD".equalsIgnoreCase(entry.getKey())) {
                databaseVariablesMap.put("javax.persistence.jdbc.password", entry.getValue());
            }
        }
        return databaseVariablesMap;
    }

    @Produces
    @RequestScoped
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public void closeEM(@Disposes EntityManager manager) {
        manager.close();
    }
}
