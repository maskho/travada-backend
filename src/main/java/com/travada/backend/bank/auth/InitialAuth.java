package com.travada.backend.bank.auth;

import com.travada.backend.bank.chache.AuthToken;
import com.travada.backend.bank.chache.RedisTokenRepository;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InitialAuth {

    private String code;

    private String password;

    @Autowired
    private RedisTokenRepository tokenRepository;

    @Autowired
    public InitialAuth(@Value("${bank.cred.code}") String code,
                       @Value("${bank.cred.password}") String password) {
        this.code = code;
        this.password = password;
    }

    public String bankInitAuth() {
        RestTemplate restTemplate = new RestTemplate();

        // create request body
        JSONObject request = new JSONObject();
        request.put("code", code);
        request.put("password", password);

        String urlString = "http://api.bank.binar.ariefdfaltah.com/application/auth/login";

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        // send request and parse result
        ResponseEntity<String> loginResponse = restTemplate
                .exchange(urlString, HttpMethod.POST, entity, String.class);
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            JSONObject userJson = new JSONObject(loginResponse.getBody());
            JSONObject result = userJson.getJSONObject("data");
            String finalResult = result.get("token").toString();

            AuthToken authToken = new AuthToken("authLogin", finalResult);
            tokenRepository.create(authToken);

            return finalResult;
        } else {
            return "Something error.";
        }
    }
}
