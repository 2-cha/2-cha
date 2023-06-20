package com._2cha.demo.member.service;

import static com._2cha.demo.member.config.NicknameGeneratorConfig.MIDDLE_PATH;
import static com._2cha.demo.member.config.NicknameGeneratorConfig.PREFIX_PATH;
import static com._2cha.demo.member.config.NicknameGeneratorConfig.SUFFIX_PATH;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NicknameGeneratorService {

  private final List<String> prefixPool = new ArrayList<>();
  private final List<String> middlePool = new ArrayList<>();
  private final List<String> suffixPool = new ArrayList<>();

  public NicknameGeneratorService() {
    try (var prefix = new ClassPathResource(PREFIX_PATH).getInputStream();
         var middle = new ClassPathResource(MIDDLE_PATH).getInputStream();
         var suffix = new ClassPathResource(SUFFIX_PATH).getInputStream()) {
      BufferedReader prefixReader = new BufferedReader(new InputStreamReader(prefix));
      BufferedReader middleReader = new BufferedReader(new InputStreamReader(middle));
      BufferedReader suffixReader = new BufferedReader(new InputStreamReader(suffix));
      prefixReader.lines().forEach(prefixPool::add);
      middleReader.lines().forEach(middlePool::add);
      suffixReader.lines().forEach(suffixPool::add);
    } catch (
        Exception e) {
      throw new BeanInitializationException("Failed to initialize NicknameGenerator", e);
    }
  }

  public String generate() {
    return randomPick(prefixPool).trim() + " " +
           randomPick(middlePool).trim() + " " +
           randomPick(suffixPool).trim();
  }

  public List<String> generate(int size) {
    List<String> result = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      result.add(randomPick(prefixPool).trim() + " " +
                 randomPick(middlePool).trim() + " " +
                 randomPick(suffixPool).trim());
    }
    return result;
  }

  public String randomPick(List<String> pool) {
    return pool.get((int) (Math.random() * pool.size()));
  }
}
