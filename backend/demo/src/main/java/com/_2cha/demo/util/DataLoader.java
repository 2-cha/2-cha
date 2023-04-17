package com._2cha.demo.util;

import com._2cha.demo.member.domain.Achievement;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.repository.AchievementRepository;
import com._2cha.demo.member.repository.MemberRepository;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.repository.PlaceRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {


  private final PlaceRepository placeRepository;
  private final MemberRepository memberRepository;
  private final AchievementRepository achvRepository;


  private final EntityManager em;

  @Transactional
  @Override
  public void run(ApplicationArguments args) throws Exception {
    em.createNativeQuery("CREATE EXTENSION postgis;");

    //XXX
    loadCsv("와인바", Category.WINE_BAR);
    loadCsv("위스키바", Category.WHISKEY_BAR);
    loadCsv("칵테일바", Category.COCKTAIL_BAR);
    createMockMember();
  }

  @Transactional
  public void createMockMember() {

    try {
      achvRepository.save(Achievement.createMockAchievement());
      Member admin = Member.createMember("admin@2cha.com", BCryptHashingUtils.hash("1234"),
                                         "admin");
      Member member = Member.createMember("member@2cha.com", BCryptHashingUtils.hash("1234"),
                                          "member");
      memberRepository.save(admin);
      memberRepository.save(member);
    } catch (EntityExistsException e) {
      
    }
  }

  @Transactional
  protected void loadCsv(String filename, Category category) throws IOException {
    Resource resource = new ClassPathResource("csv/" + filename + ".csv");
    InputStream input = resource.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

    reader.readLine();
    String line;
    while ((line = reader.readLine()) != null) {
      String[] columns = line.split("\",\"");

      for (int i = 0; i < columns.length; i++) {
        columns[i] = StringUtils.deleteAny(columns[i], "\"");
      }

      String name = columns[0];
      String address = columns[2];
      Double latitude = Double.parseDouble(columns[6]);
      Double longitude = Double.parseDouble(columns[7]);
      String thumbnail = columns[8];
      Place place = Place.createPlace(
          name,
          category,
          address,
          longitude,
          latitude,
          thumbnail
                                     );
      try {
        placeRepository.save(place);
      } catch (EntityExistsException e) {

      }
    }
  }
}
