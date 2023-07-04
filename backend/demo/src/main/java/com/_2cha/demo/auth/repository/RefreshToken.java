package com._2cha.demo.auth.repository;

import com._2cha.demo.auth.config.JwtConfig;
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

  @Id
  private Long id;

  private List<String> values;

  @TimeToLive
  private Long ttl = JwtConfig.REFRESH_LIFETIME.longValue() * 60;

  public RefreshToken(Long id, List<String> values) {
    this.id = id;
    this.values = values;
  }

  public void addToken(String value) {
    this.values.add(value);
  }
}
