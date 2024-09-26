package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CreateTask {

    private static final String URL = "http://localhost:80/jsonrpc.php";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String AUTHORIZATION_HEADER = getAuthorizationHeader();

    private static String getAuthorizationHeader() {
        String auth = USERNAME + ":" + PASSWORD;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void createTask() {
        String taskTitle = "PMTask";
        int ownerId = 1;
        int projectId = 10;
        createTaskKanboard(taskTitle, ownerId, projectId);
    }

    private void createTaskKanboard(String taskTitle, int ownerId, int projectId) {
        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createTask\",\n" +
                "    \"id\": 1,\n" +
                "    \"params\": {\n" +
                "        \"title\": \"" + taskTitle + "\",\n" +
                "        \"owner_id\": " + ownerId + ",\n" +
                "        \"project_id\": " + projectId + "\n" +
                "    }\n" +
                "}";
        Response response = sendCreateTaskRequest(json);
        logResponse(response);
    }

    private Response sendCreateTaskRequest(String json) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", AUTHORIZATION_HEADER)
                .body(json)
                .post(URL);
    }

    private void logResponse(Response response) {
        System.out.println("Response Status: " + response.getStatusLine());
        System.out.println("Response Body: " + response.asString());
        if (response.getStatusCode() != 200) {
            System.err.println("Під час створення задачі сталася помилка. Код статусу: " + response.getStatusCode());
        }
    }
}
