package com._2cha.demo.member.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relationship {

  /*-----------
   @ Columns
   ----------*/
  @Id
  @ManyToOne
  @JoinColumn(name = "FOLLOWER_ID")
  private Member follower;

  @Id
  @ManyToOne
  @JoinColumn(name = "FOLLOWING_ID")
  private Member following;

  /*-----------
   @ Methods
   ----------*/

  public static Relationship createRelationship(Member follower, Member following) {
    Relationship relationship = new Relationship();
    relationship.follower = follower;
    relationship.following = following;
    return relationship;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Relationship that)) {
      return false;
    }
    return follower.equals(that.follower) && following.equals(that.following);
  }

  @Override
  public int hashCode() {
    return Objects.hash(follower, following);
  }
}
