package com.travada.backend.bank.request;

import com.travada.backend.bank.service.EncodePayload;
import com.travada.backend.bank.auth.ThirdPartyAuth;
import com.travada.backend.bank.chache.RedisTokenRepository;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.travada.backend.bank.chache.RedisConfig.redisTemplate;

@Component
public class RequestService {

    @Autowired
    private EncodePayload encodePayload;

    @Autowired
    private ThirdPartyAuth thirdPartyAuth;


    public String requestBalance(String accountNumber) {

        RestTemplate restTemplate = new RestTemplate();
        RedisTokenRepository tokenRepository = new RedisTokenRepository(redisTemplate());

        String tokenTP = tokenRepository.get("authTP").getToken();

        //Set data payload
        String data = encodePayload.singleLogin(accountNumber);

        // create request body
        JSONObject request = new JSONObject();
        request.put("data", data);

        String urlString = "http://api.bank.binar.ariefdfaltah.com/bank/account/balance/request";

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenTP);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        // send request and parse result
        ResponseEntity<String> loginResponse = restTemplate
                .exchange(urlString, HttpMethod.POST, entity, String.class);
        if (loginResponse.getStatusCode() == HttpStatus.CREATED) {
            JSONObject userJson = new JSONObject(loginResponse.getBody());
            JSONObject result = userJson.getJSONObject("data");
            String finalResult = result.get("request_id").toString();

            return finalResult;
        } else {
            return "Something error.";
        }
    }

    public String requestTransfer(String numberOrigin, String numberDestination, String transferNominal) {

        RestTemplate restTemplate = new RestTemplate();
        RedisTokenRepository tokenRepository = new RedisTokenRepository(redisTemplate());

        String tokenTP = tokenRepository.get("authTP").getToken();

        //Set data payload
        String data = encodePayload.encodeTransfer(numberOrigin, numberDestination, transferNominal);

        // create request body
        JSONObject request = new JSONObject();
        request.put("data", data);

        String urlString = "http://api.bank.binar.ariefdfaltah.com/bank/account/transfer/request";

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenTP);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        // send request and parse result
        ResponseEntity<String> loginResponse = restTemplate
                .exchange(urlString, HttpMethod.POST, entity, String.class);
        if (loginResponse.getStatusCode() == HttpStatus.CREATED) {
            JSONObject userJson = new JSONObject(loginResponse.getBody());
            JSONObject result = userJson.getJSONObject("data");
            String finalResult = result.get("request_id").toString();

            return finalResult;
        } else {
            return "Something error.";
        }


    }
}
