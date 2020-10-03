package com.travada.backend.bank.auth;

import com.travada.backend.bank.chache.AuthToken;
import com.travada.backend.bank.service.EncodePayload;
import com.travada.backend.bank.chache.RedisTokenRepository;
import com.travada.backend.utils.crypto.EncoderHelper;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.travada.backend.bank.chache.RedisConfig.redisTemplate;

@Component
public class ThirdPartyAuth {

    @Autowired
    private EncoderHelper encoderHelper;

    @Autowired
    private InitialAuth initialAuth;

    @Autowired
    EncodePayload encodePayload;

    @Autowired
    private RedisTokenRepository tokenRepository;


    public String thirdPartyAuth() {
        RestTemplate restTemplate = new RestTemplate();
        RedisTokenRepository tokenRepository = new RedisTokenRepository(redisTemplate());

        //Set data payload
        String data = encodePayload.appLogin("travada","2020travada0922" );

        //get_login_token
        String loginToken = tokenRepository.get("authLogin").getToken();

        // create request body
        JSONObject request = new JSONObject();
        request.put("data", data);

        String urlString = "http://api.bank.binar.ariefdfaltah.com/third/party/authorize";

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        // send request and parse result
        ResponseEntity<String> loginResponse = restTemplate
                .exchange(urlString, HttpMethod.POST, entity, String.class);
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            JSONObject userJson = new JSONObject(loginResponse.getBody());
            JSONObject result = userJson.getJSONObject("data");
            String finalResult = result.get("auth_token").toString();

            AuthToken authToken = new AuthToken("authTP", finalResult);
            tokenRepository.create(authToken);

            return finalResult;
        } else {
            return "Something error.";
        }
    }
}
