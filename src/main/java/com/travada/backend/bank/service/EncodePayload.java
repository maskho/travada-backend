package com.travada.backend.bank.service;

import com.travada.backend.utils.crypto.EncoderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncodePayload {

    private String bankSec;

    private String bankPub;

    @Autowired
    public EncodePayload(@Value("${bank.cred.secret}") String secret,
                         @Value("${bank.cred.pubkey}") String pubkey) {
        this.bankSec = secret;
        this.bankPub = pubkey;
    }

    @Autowired
    private EncoderHelper encoderHelper;

    public String appLogin(String code, String password) {

        //raw_data
        String rawCode = encoderHelper.encryptBase64(code);
        String rawPassword = encoderHelper.encryptBase64(password);
        String rawData = (rawCode + ":" + rawPassword);

        //pre_encrypt_data
        String preEncrypt = encoderHelper.encryptAES256(rawData, bankSec);

        //pre_send_data
        String preSend = (preEncrypt + ":" + bankPub);

        //send_data
        String data = encoderHelper.encryptBase64(preSend);
        return data;
    }

    public String getLogin(String code, String password) {
        //Set data payload

        //raw_data
        String rawPassword = encoderHelper.encryptBase64(password);
        String rawData = (code + ":" + rawPassword);

        //pre_encrypt_data
        String preEncrypt = encoderHelper.encryptAES256(rawData, bankSec);

        //pre_send_data
        String preSend = (preEncrypt + ":" + bankPub);

        //send_data
        String data = encoderHelper.encryptBase64(preSend);
        return data;
    }

    public String encodeTransfer(String numberOrigin, String numberDestination, String transferNominal) {

        //raw_data
        String rawOrigin = encoderHelper.encryptBase64(numberOrigin);
        String rawDestination = encoderHelper.encryptBase64(numberDestination);
        String rawNominal = encoderHelper.encryptBase64(transferNominal.toString());
        String rawData = (rawOrigin + ":" + rawDestination + ":" + rawNominal);

        //pre_encrypt_data
        String preEncrypt = encoderHelper.encryptAES256(rawData, bankSec);

        //pre_send_data
        String preSend = (preEncrypt + ":" + bankPub);

        //send_data
        String data = encoderHelper.encryptBase64(preSend);
        return data;
    }


    public String singleLogin(String payload) {
        //Set data payload

        //raw_data
        String rawCode = encoderHelper.encryptBase64(payload);
        String rawData = (rawCode);

        //pre_encrypt_data
        String preEncrypt = encoderHelper.encryptAES256(rawData, bankSec);

        //pre_send_data
        String preSend = (preEncrypt + ":" + bankPub);

        //send_data
        String data = encoderHelper.encryptBase64(preSend);
        return data;
    }
}
