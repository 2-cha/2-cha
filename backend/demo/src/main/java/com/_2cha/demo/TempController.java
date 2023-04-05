package com._2cha.demo;

import com._2cha.demo.auth.config.GoogleOIDCConfig;
import com._2cha.demo.auth.strategy.oidc.GoogleOIDCStrategy;
import com._2cha.demo.member.domain.OIDCProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TempController {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  GoogleOIDCConfig oidcConfig;

  @GetMapping(value = "front/{provider}/callback")
  public String frontCallback(@PathVariable OIDCProvider provider,
                              @RequestParam String code,
                              HttpServletResponse response) throws IOException {
    GoogleOIDCStrategy strategy = new GoogleOIDCStrategy(objectMapper, oidcConfig);

    return code;
  }

  @GetMapping(value = "front/{provider}/login")
  public String front(@PathVariable OIDCProvider provider,
                      @RequestParam Map<String, String> queries,
                      HttpServletResponse response) throws IOException {
    GoogleOIDCStrategy strategy = new GoogleOIDCStrategy(objectMapper, oidcConfig);
    response.sendRedirect(strategy.getAuthCode());
    return "Bye";
  }
}

