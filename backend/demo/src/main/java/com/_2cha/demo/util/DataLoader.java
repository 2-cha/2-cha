package com._2cha.demo.util;

import com._2cha.demo.member.domain.Achievement;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.repository.AchievementRepository;
import com._2cha.demo.member.repository.MemberRepository;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.repository.PlaceRepository;
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


  @Transactional
  @Override
  public void run(ApplicationArguments args) throws Exception {
    //XXX
    loadCsv("와인바", Category.WINE_BAR);
    //loadCsv("위스키바", Category.WHISKEY_BAR);
    //loadCsv("칵테일바", Category.COCKTAIL_BAR);
    //createMockMember();
  }

  @Transactional
  public void createMockMember() {
    Achievement achievement = Achievement.createAchievement("Rising Star",
                                                            "You got first follower",
                                                            "https://picsum.photos/64/64");
    achvRepository.save(achievement);
    Member admin = Member.createMember("admin@2cha.com", BCryptHashingUtils.hash("1234"), "admin");
    Member member = Member.createMember("member@2cha.com", BCryptHashingUtils.hash("1234"),
                                        "member");
    memberRepository.save(admin);
    memberRepository.save(member);
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
      String lotAddress = columns[3];
      String site = columns[5];
      Double latitude = Double.parseDouble(columns[6]);
      Double longitude = Double.parseDouble(columns[7]);
      String thumbnail = columns[8];
      Place place = Place.createPlace(
          name,
          category,
          address,
          lotAddress,
          longitude,
          latitude,
          thumbnail,
          thumbnail,
          site);
      placeRepository.save(place);
    }
  }
}
