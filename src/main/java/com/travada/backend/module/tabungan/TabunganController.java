package com.travada.backend.module.tabungan;

import com.travada.backend.config.security.CurrentUser;
import com.travada.backend.config.security.UserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TabunganController {

    @GetMapping("/tabungan")
    public String getTabungan(@CurrentUser UserPrincipal userPrincipal) {

        return userPrincipal.getUsername();
    }
}
