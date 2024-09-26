package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CreateProject {

    private static final String URL = "http://localhost:80/jsonrpc.php";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private static String getAuthorizationHeader() {
        String auth = USERNAME + ":" + PASSWORD;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void createProject() {
        String projectName = "Board_2.0";

        createProjectKanboard(projectName, 1);
    }

    private void createProjectKanboard(String projectName, int ownerId) {
        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createProject\",\n" +
                "    \"id\": 1,\n" +
                "    \"params\": {\n" +
                "        \"name\": \"" + projectName + "\",\n" +
                "        \"owner_id\": " + ownerId + "\n" +
                "    }\n" +
                "}";

        Response response = sendCreateProjectRequest(json);

        System.out.println("Response Status: " + response.getStatusLine());
        System.out.println("Response Body: " + response.asString());
    }

    private Response sendCreateProjectRequest(String json) {
        String authorizationHeader = getAuthorizationHeader();

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", authorizationHeader)
                .body(json)
                .post(URL);
    }
}
