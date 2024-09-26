package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

    public class DeleteUser {

        private static final String URL = "http://localhost:80/jsonrpc.php";
        private static final String USERNAME = "admin";
        private static final String PASSWORD = "admin";

        private static String getAuthorizationHeader() {
            String auth = USERNAME + ":" + PASSWORD;
            return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        }

        @Test
    public void removeUser() {
        int userId = 4;
        removeUserById(userId);
    }

        /*@Test
        public void deleteUsers() {
            for (int userId = 2; userId <= 10; userId++) {
                if (isUserExists(userId)) {
                    removeUserById(userId);
                } else {
                    System.out.println("Користувач з ID " + userId + " не існує.");
                }
            }
        }*/

        private boolean isUserExists(int userId) {
            String json = "{\n" +
                    "    \"jsonrpc\": \"2.0\",\n" +
                    "    \"method\": \"getUserById\",\n" +
                    "    \"id\": 2,\n" +
                    "    \"params\": {\n" +
                    "        \"user_id\": " + userId + "\n" +
                    "    }\n" +
                    "}";

            Response response = sendCheckUserRequest(json);
            return response.getStatusCode() == 200 && response.getBody().jsonPath().get("result") != null;
        }

        private void removeUserById(int userId) {
            String json = "{\n" +
                    "    \"jsonrpc\": \"2.0\",\n" +
                    "    \"method\": \"removeUser\",\n" +
                    "    \"id\": 2,\n" +
                    "    \"params\": {\n" +
                    "        \"user_id\": " + userId + "\n" +
                    "    }\n" +
                    "}";

            Response response = sendRemoveUserRequest(json);
            logResponse(response, userId);
        }

        private Response sendCheckUserRequest(String json) {
            return RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", getAuthorizationHeader())
                    .body(json)
                    .post(URL);
        }

        private Response sendRemoveUserRequest(String json) {
            return RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", getAuthorizationHeader())
                    .body(json)
                    .post(URL);
        }

        private void logResponse(Response response, int userId) {
            System.out.println("User ID: " + userId);
            System.out.println("Response Status: " + response.getStatusLine());
            System.out.println("Response Body: " + response.asString());
            if (response.getStatusCode() != 200) {
                System.err.println("Під час видалення користувача сталася помилка. Код статусу: " + response.getStatusCode());
            }
        }
    }
