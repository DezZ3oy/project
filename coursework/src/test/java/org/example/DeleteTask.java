package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DeleteTask {

    private static final String URL = "http://localhost:80/jsonrpc.php";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String AUTHORIZATION_HEADER = getAuthorizationHeader();
    private static final int TASK_ID = 1;

    private static String getAuthorizationHeader() {
        String auth = USERNAME + ":" + PASSWORD;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void closeTask() {
        System.out.println("Була закрита задача №: " + TASK_ID);
        closeTaskInKanboard(TASK_ID);
    }

    private void closeTaskInKanboard(int taskId) {
        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"closeTask\",\n" +
                "    \"id\": 1,\n" +
                "    \"params\": {\n" +
                "        \"task_id\": " + taskId + "\n" +
                "    }\n" +
                "}";

        Response response = sendCloseTaskRequest(json);
        logResponse(response);
    }

    private Response sendCloseTaskRequest(String json) {
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
            System.err.println("Під час закриття задачі сталася помилка. Код статусу: " + response.getStatusCode());
        }
    }
}
