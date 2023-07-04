package com._2cha.demo.auth.repository;

import com._2cha.demo.auth.config.JwtConfig;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "auth")
@Getter
@Setter
public class RefreshToken {

  private static final int MAX_REFRESH_TOKEN_SIZE = 5;

  @Id
  private Long id;

  private List<String> values = new ArrayList<>();

  @TimeToLive
  private Long ttl = JwtConfig.REFRESH_LIFETIME.longValue() * 60;

  public RefreshToken(Long id, List<String> values) {
    this.id = id;
    this.values.addAll(values);
  }

  public void addToken(String value) {
    if (this.values.size() == MAX_REFRESH_TOKEN_SIZE) {
      this.values.remove(0);
    }
    this.values.add(value);
  }
}
