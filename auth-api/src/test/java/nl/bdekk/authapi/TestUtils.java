package nl.bdekk.authapi;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TestUtils {

    public static final String URL = "http://localhost:8081";

//    public static final Header AUTHORIZATION_HEADER = new Header("Authorization", "Bearer QafmHNtm0IGZ3kEBecpDYz5XlfLEjd9EL4RjAswz");

    public static <T> T readFileToObject(String fileName, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream is = TextUtils.class.getResourceAsStream( fileName);
        T object = objectMapper.readValue(is, clazz);
        return object;
    }

    public static JsonNode readFileToJSON(String fileName) {
        InputStream is = TextUtils.class.getClassLoader().getResourceAsStream( fileName);
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String jsonText = s.hasNext() ? s.next() : "";

        ObjectMapper mapper = new ObjectMapper();

        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actualObj;
    }
}