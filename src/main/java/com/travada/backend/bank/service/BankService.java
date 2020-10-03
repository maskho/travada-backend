package com.travada.backend.bank.service;

import com.travada.backend.bank.auth.ThirdPartyAuth;
import com.travada.backend.bank.chache.RedisTokenRepository;
import com.travada.backend.bank.request.RequestService;
import com.travada.backend.utils.crypto.EncoderHelper;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.travada.backend.bank.chache.RedisConfig.redisTemplate;

@Component
public class BankService {

    private RestTemplate restTemplate;
    private RedisTokenRepository tokenRepository;
    private String initialToken;

    @Autowired
    private EncodePayload encodePayload;

    @Autowired
    private ThirdPartyAuth thirdPartyAuth;

    @Autowired
    private RequestService requestService;

    @Autowired
    private EncoderHelper encoderHelper;

    @Autowired
    public BankService() {
        this.restTemplate = new RestTemplate();
        this.tokenRepository = new RedisTokenRepository(redisTemplate());
    }

    public String getBalance(String accountNumber, String accountPin) {

        String tokenTP = tokenRepository.get("authTP").getToken();

        String requestId = requestService.requestBalance(accountNumber);

        //Set data payload
        String data = encodePayload.getLogin(requestId, accountPin);

        // create request body
        JSONObject request = new JSONObject();
        request.put("data", data);

        String urlString = "http://api.bank.binar.ariefdfaltah.com/bank/account/balance";

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
            String finalResult = result.get("balance").toString();

            return finalResult;
        } else {
            return "Something error.";
        }
    }

    public String doTransfer(String numberOrigin, String numberDestination, String transferNominal, String accountPin) {

        String tokenTP = tokenRepository.get("authTP").getToken();

        RestTemplate restTemplate = new RestTemplate();
        RedisTokenRepository tokenRepository = new RedisTokenRepository(redisTemplate());

        String requestId = requestService.requestTransfer(numberOrigin, numberDestination, transferNominal);

        //Set data payload
        String data = encodePayload.getLogin(requestId, accountPin);

        // create request body
        JSONObject request = new JSONObject();
        request.put("data", data);

        String urlString = "http://api.bank.binar.ariefdfaltah.com/bank/account/transfer";

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
            String finalResult = userJson.get("message").toString();

            return finalResult;
        } else {
            return "Something error.";
        }


    }
}
