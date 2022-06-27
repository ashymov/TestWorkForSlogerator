package com.gmail.sashymov.TestWorkForSlogerator.apiTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.sashymov.TestWorkForSlogerator.apiTest.bodies.AuthBody;
import com.gmail.sashymov.TestWorkForSlogerator.apiTest.bodies.GetGuestTokenBody;
import com.gmail.sashymov.TestWorkForSlogerator.apiTest.bodies.RegisterPlayerBody;
import com.gmail.sashymov.TestWorkForSlogerator.apiTest.responses.AuthResponse;
import com.gmail.sashymov.TestWorkForSlogerator.apiTest.responses.GetTokenResponse;
import com.gmail.sashymov.TestWorkForSlogerator.apiTest.responses.PlayerResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import static com.gmail.sashymov.TestWorkForSlogerator.settings.Constants.*;
import static io.restassured.RestAssured.given;

public class ApiTest {
    ObjectMapper objectMapper = new ObjectMapper();


    RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(MAIN_URL)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    public void testCases() throws JsonProcessingException {

        GetGuestTokenBody guestTokenBody = new GetGuestTokenBody();
        guestTokenBody.setGrant_type("client_credentials");
        guestTokenBody.setScope("guest:default");

//отправляем запрос на получение гостевого токена
        Response response =
                given()

                        .spec(requestSpec)
                        .header("Authorization", BASIC_AUTH)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(guestTokenBody)
                        .when()
                        .post(TOKEN_REQUEST_PATH)
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract().response();

        String s = response.getBody().asString();
        GetTokenResponse getTokenResponse = objectMapper.readValue(s, GetTokenResponse.class);
        //гостевой токен
        String guestToken = getTokenResponse.getAccess_token();


        RegisterPlayerBody registerBody = new RegisterPlayerBody();

        String password = "password";

        String encodedPass = new String(Base64.encodeBase64(password.getBytes()));

        registerBody.setEmail("qwertuio@gmail.com");
        registerBody.setName("TestName");
        registerBody.setSurname("TestSurname");
        registerBody.setUsername("qwertuio");
        registerBody.setPassword_change(encodedPass);
        registerBody.setPassword_repeat(encodedPass);

        //Отправляем запрос на создание нового игрока
        Response registerPlayerResponse =
                given()
                        .spec(requestSpec)
                        .header("Authorization", getTokenResponse.getToken_type() + " " + getTokenResponse.getAccess_token())
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(registerBody)
                        .when()
                        .post(PLAYERS_REQUEST_PATH)
                        .then()
                        .statusCode(201)
                        .log().all()
                        .extract().response();

        String player = registerPlayerResponse.getBody().asString();
        PlayerResponse playerResponse = objectMapper.readValue(player, PlayerResponse.class);
// игрок создан всё ок

        AuthBody authBody = new AuthBody();
        authBody.setGrant_type(password);
        authBody.setPassword(registerBody.getPassword_change());
        authBody.setUsername(registerBody.getUsername());
//авторизация созданным игроком
        Response authResponse =
                given()
                        .spec(requestSpec)
                        .header("Authorization", BASIC_AUTH)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(authBody)
                        .when()
                        .post(TOKEN_REQUEST_PATH)
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract().response();

        String auth = authResponse.getBody().asString();
        //авторизация прошла успешно и получаем токен игрока
        AuthResponse playerInfo = objectMapper.readValue(auth, AuthResponse.class);


        //запрос на получение инфо об игроке используя его токен
        Response getRegisterPlayerInfo =
                given()
                        .spec(requestSpec)
                        .header("Authorization", "Bearer " + playerInfo.getAccess_token())
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .when()
                        .get(PLAYERS_REQUEST_PATH + "/" + playerResponse.getId())
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract().response();


        //запрос на получение инфо другого игрока
        Response getAnotherPlayerInfo =
                given()
                        .spec(requestSpec)
                        .header("Authorization", "Bearer " + playerInfo.getAccess_token())
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .when()
                        .get(PLAYERS_REQUEST_PATH + "/" + 125)
                        .then()
                        .statusCode(404)
                        .log().all()
                        .extract().response();
        //получаем корректный ответ и статус код 404
    }

}


