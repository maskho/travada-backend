package com.travada.backend.utils.crypto;

import org.cloudinary.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class EncoderHelper {

    public String encryptAES256(String data, String key) {

        RestTemplate restTemplate = new RestTemplate();

        // create request body
        JSONObject request = new JSONObject();
        request.put("data", data);
        request.put("key", key);

        String urlString = "http://157.245.56.50:3000/encrypt/aes256";

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        // send request and parse result
        ResponseEntity<String> loginResponse = restTemplate
                .exchange(urlString, HttpMethod.POST, entity, String.class);
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            JSONObject userJson = new JSONObject(loginResponse.getBody());
            String result = userJson.get("result").toString();

            return  result;
        } else {
            return "Something error.";
        }
    }

    public String encryptBase64(String data) {
        String encrypted = Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
       return encrypted;
    }

}
