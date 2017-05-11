package nl.bdekk.writeapi.projects;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import nl.bdekk.writeapi.TestUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class FileIT {

    @Test
    public void getFile() {
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
    public void updateFile() {
        JsonNode json = TestUtils.readFileToJSON("valid-file.json");

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put(TestUtils.URL + "/file/1")
                .then()
                .statusCode(200)
                .body("content", equalTo("Content"), "name", equalTo("file1"));
    }

    @Test
    @Ignore
    public void createFile() {
        JsonNode json = TestUtils.readFileToJSON("valid-file.json");

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(TestUtils.URL + "/file")
                .then()
                .statusCode(201)
                .body("content", equalTo("Content"), "name", equalTo("file1"));
    }
}
