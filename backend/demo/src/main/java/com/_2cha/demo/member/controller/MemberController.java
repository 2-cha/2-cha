package com._2cha.demo.member.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.global.infra.imageupload.dto.ImageSavedResponse;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.global.validator.imagemime.ImageMime;
import com._2cha.demo.member.dto.AchievementResponse;
import com._2cha.demo.member.dto.MemberImageUpdateRequest;
import com._2cha.demo.member.dto.MemberInfoResponse;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.dto.MemberProfileUpdateRequest;
import com._2cha.demo.member.dto.RelationshipOperationResponse;
import com._2cha.demo.member.dto.SignUpRequest;
import com._2cha.demo.member.dto.ToggleAchievementExposureRequest;
import com._2cha.demo.member.dto.ToggleAchievementExposureResponse;
import com._2cha.demo.member.service.MemberService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final ImageUploadService imageUploadService;

  @Auth
  @GetMapping("/{memberId}")
  public MemberProfileResponse getMemberProfile(@PathVariable Long memberId) {
    return memberService.getMemberProfileById(memberId);
  }

  @Auth
  @PutMapping("")
  public MemberProfileResponse updateMemberProfile(@Authed Long memberId,
                                                   @Valid @RequestBody MemberProfileUpdateRequest dto) {
    return memberService.updateProfile(memberId, dto.getName(), dto.getProfMsg());
  }

  @Auth
  @PostMapping("/image")
  public CompletableFuture<ImageSavedResponse> uploadMemberImage(@Authed Long memberId,
                                                                 @ImageMime MultipartFile file)
      throws IOException {
    return imageUploadService.saveThumbnail(file.getBytes());
  }

  @Auth
  @PutMapping("/image")
  public MemberProfileResponse updateMemberImageUrl(@Authed Long memberId,
                                                    @Valid @RequestBody MemberImageUpdateRequest dto) {
    return memberService.updateProfileImage(memberId, dto.getUrl());
  }

  @Auth
  @GetMapping("/achievements")
  public List<AchievementResponse> getAllMemberAchievements(@Authed Long memberId) {
    return memberService.getAllMemberAchievements(memberId);
  }

  @Auth
  @PutMapping("/achievements/exposed")
  public List<ToggleAchievementExposureResponse> toggleAchvsExposure(@Authed Long memberId,
                                                                     @Valid @RequestBody List<ToggleAchievementExposureRequest> dto
                                                                    ) {

    Map<Long, Boolean> achvExposureMap = dto.stream().collect(
        Collectors.toMap(d -> d.getAchvId(), d -> d.isExposure()));
    return memberService.toggleAchievementsExposure(memberId, achvExposureMap);
  }

  @Auth
  @GetMapping("/{memberId}/achievements")
  public List<AchievementResponse> getExposedMemberAchievements(@PathVariable Long memberId) {
    return memberService.getExposedMemberAchievements(memberId);
  }


  @Auth
  @GetMapping("/{memberId}/followers")
  public List<MemberProfileResponse> getFollowers(@PathVariable Long memberId) {
    return memberService.getFollowers(memberId);
  }

  @Auth
  @GetMapping("/{memberId}/followings")
  public List<MemberProfileResponse> getFollowings(@PathVariable Long memberId) {
    return memberService.getFollowings(memberId);
  }


  @PostMapping
  public MemberInfoResponse signUp(@Valid @RequestBody SignUpRequest dto) {
    return memberService.signUp(dto.getEmail(), dto.getName(), dto.getPassword());
  }

  @Auth
  @PostMapping("/{followingId}/follow")
  public RelationshipOperationResponse follow(@Authed Long followerId,
                                              @PathVariable Long followingId) {
    return memberService.follow(followerId, followingId);
  }

  @Auth
  @DeleteMapping("/{followingId}/follow")
  public RelationshipOperationResponse unfollow(@Authed Long followerId,
                                                @PathVariable Long followingId) {
    return memberService.unfollow(followerId, followingId);
  }
}
