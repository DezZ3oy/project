package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DeleteProject {

    private static final String URL = "http://localhost:80/jsonrpc.php";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private static String getAuthorizationHeader() {
        String auth = USERNAME + ":" + PASSWORD;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    /*@Test
    public void removeProject() {
        int projectId = 1;
        if (isProjectExists(projectId)) {
            deleteProjectKanboard(projectId);
        } else {
            System.out.println("Проєкт з ID " + projectId + " не існує.");
        }
    }*/

    @Test
    public void deleteProjects() {
        for (int projectId = 10; projectId <= 30; projectId++) {
            if (isProjectExists(projectId)) {
                deleteProjectKanboard(projectId);
            } else {
                System.out.println("Проєкт з ID " + projectId + " не існує.");
            }
        }
    }

    private boolean isProjectExists(int projectId) {
        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"getProjectById\",\n" +
                "    \"id\": 1,\n" +
                "    \"params\": {\n" +
                "        \"project_id\": " + projectId + "\n" +
                "    }\n" +
                "}";

        Response response = sendRequest(json);
        System.out.println("Response Status (Check Project Exists): " + response.getStatusLine());

        return response.getStatusCode() == 200 && response.getBody().jsonPath().get("result") != null;
    }

    private void deleteProjectKanboard(int projectId) {
        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeProject\",\n" +
                "    \"id\": 1,\n" +
                "    \"params\": {\n" +
                "        \"project_id\": " + projectId + "\n" +
                "    }\n" +
                "}";

        Response response = sendRequest(json);
        System.out.println("Response Status: " + response.getStatusLine());
        System.out.println("Response Body: " + response.asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Очікувався статус 200 при видаленні проекту з ID " + projectId + ".");
    }

    private Response sendRequest(String json) {
        String authorizationHeader = getAuthorizationHeader();

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", authorizationHeader)
                .body(json)
                .post(URL);
    }
}
