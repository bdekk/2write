package nl.bdekk.authapi.users;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import nl.bdekk.authapi.TestUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProjectIT {

    @Test
    public void getProjects() {
        Response response = given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(TestUtils.URL + "/project").andReturn();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);
        Assert.assertNotEquals("", response.getBody().print());
    }

    @Ignore
    @Test
    public void getProject() {
        long projectId = 1;

        Response response = given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(TestUtils.URL + "/project/" + projectId).andReturn();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);
    }

    @Ignore
    @Test
    public void getProjectByUser() {
        Response response = given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(TestUtils.URL + "/project").andReturn();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);
        Assert.assertNotEquals("", response.getBody().print());
    }

    @Test
    public void createProject() {
        JsonNode json = TestUtils.readFileToJSON("valid-project.json");

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(TestUtils.URL + "/project")
                .then()
                .statusCode(201)
                .body("title", equalTo("book1"), "description", equalTo("Book1 project"));
    }

    @Ignore
    @Test
    public void createDuplicateProject() {

        JsonNode json = TestUtils.readFileToJSON("valid-project.json");
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(TestUtils.URL + "/project")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(json.asText())
                .when()
                .post(TestUtils.URL + "/project")
                .then()
                .statusCode(409);
    }


}
