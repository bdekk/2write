package nl.bdekk.authapi.users;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import nl.bdekk.authapi.TestUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class UserIT {

    @Test
    public void createUser() {
        JsonNode json = TestUtils.readFileToJSON("valid-user.json");

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(TestUtils.URL + "/user")
                .then()
                .statusCode(201);
    }


}
