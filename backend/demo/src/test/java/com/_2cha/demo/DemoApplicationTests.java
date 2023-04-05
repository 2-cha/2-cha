package com._2cha.demo;


import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

  @Autowired
  Faker faker;

  @Test
  void contextLoads() {
    System.out.println(faker.food().dish());
  }
}
