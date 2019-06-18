package en.ubb.authorization.config;

import en.ubb.authorization.AuthorizationServerApplication;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AuthorizationServerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TTest {

    @Autowired
    private TokenStore tokenStore;

    @Test
    public void whenTokenDoesNotContainIssuer_thenSuccess() {
        String tokenValue = obtainAccessToken("fooClientIdPassword", "root", "12345678");
        OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
        Map<String, Object> details = (Map<String, Object>) auth.getDetails();
        System.out.println(details);
    }

    private String obtainAccessToken(
            String clientId, String username, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        Response response = RestAssured.given()
                .auth().preemptive().basic(clientId, "secret")
                .and().with().params(params).when()
                .post("http://localhost:8804/spring-security-oauth-server/oauth/token");
        return response.jsonPath().getString("access_token");
    }
}