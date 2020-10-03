package com.travada.backend.bank.chache;

import com.travada.backend.bank.auth.InitialAuth;
import com.travada.backend.bank.auth.ThirdPartyAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.travada.backend.bank.chache.RedisConfig.redisTemplate;

@Component
public class TokenScheduler {

    RedisTokenRepository tokenRepository = new RedisTokenRepository(redisTemplate());

    @Autowired
    private InitialAuth initialAuth;

    @Autowired
    private ThirdPartyAuth thirdPartyAuth;

    private final int halfHr = (60000*30);
    private final int oneHr = (60000*60);

    @Scheduled(fixedDelay = halfHr)
    public void scheduleFixedDelayTask() {

        //Set Initial Auth

        //find token
        try {
            AuthToken findLogin = tokenRepository.get("authLogin");
            AuthToken findTP = tokenRepository.get("authTP");

        if (findLogin == null || findTP == null) {
            tokenRepository.create(new AuthToken("authLogin", "travada"));
            tokenRepository.create(new AuthToken("authTP","travada"));
        }

        //get new authtoken
        String newToken = initialAuth.bankInitAuth();
        AuthToken updateToken = new AuthToken("authLogin", newToken);

        //update token to redis
        tokenRepository.update(updateToken);

        //check token
        String checkToken = tokenRepository.get("authLogin").getToken();
        System.out.println(checkToken);



        //Set Third Party Authorize


        //get new authtoken
        String thirdAuth = thirdPartyAuth.thirdPartyAuth();
        AuthToken thirdpartyToken = new AuthToken("authTP", thirdAuth);

        //update token to redis
        tokenRepository.update(thirdpartyToken);

        //check token
        String checkToken2 = tokenRepository.get("authTP").getToken();
        System.out.println(checkToken2);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
