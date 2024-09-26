package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class CreateUser {

    private static final String URL = "http://localhost:80/jsonrpc.php";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final SecureRandom random = new SecureRandom();

    private static String getAuthorizationHeader() {
        String auth = USERNAME + ":" + PASSWORD;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    private static String generateUsername() {
        int random1 = random.nextInt(2);
        int random2 = random.nextInt(9);
        return "user" + random1 + random2;
    }

    private static String generateRandomPassword(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }

    @Test
    public void createUser() {
        String username = generateUsername();
        String password = generateRandomPassword(12);

        System.out.println("Generated Username: " + username);
        System.out.println("Generated Password: " + password);

        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createUser\",\n" +
                "    \"id\": 2,\n" +
                "    \"params\": {\n" +
                "        \"username\": \"" + username + "\",\n" +
                "        \"password\": \"" + password + "\"\n" +
                "    }\n" +
                "}";

        Response response = sendCreateUserRequest(json);
        logResponse(response);
    }

    private Response sendCreateUserRequest(String json) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", getAuthorizationHeader())
                .body(json)
                .post(URL);
    }

    private void logResponse(Response response) {
        System.out.println("Response Status: " + response.getStatusLine());
        System.out.println("Response Body: " + response.asString());
        if (response.getStatusCode() != 200) {
            System.err.println("Під час створення користувача сталася помилка. Код статусу: " + response.getStatusCode());
        }
    }
}
