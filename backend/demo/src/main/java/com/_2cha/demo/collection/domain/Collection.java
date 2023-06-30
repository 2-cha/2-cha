package com._2cha.demo.collection.domain;

import static org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate.SHALLOW;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.review.domain.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;

@Entity
@Getter
@Indexed(index = "CollectionWithCorpus")
public class Collection {

  @Id
  @DocumentId
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  @FullTextField(analyzer = "nori", projectable = Projectable.YES)
  private String title;

  @Column(nullable = false)
  @FullTextField(analyzer = "nori", projectable = Projectable.YES)
  private String description;

  private boolean isExposed = true;

  @Column(nullable = false)
  private String thumbnailUrlPath;

  @BatchSize(size = 100)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReviewInCollection> reviews = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Transient
  @FullTextField(
      analyzer = "nori",
      name = "topTagMessagesCorpus",
      projectable = Projectable.YES,
      valueBridge = @ValueBridgeRef(type = TopTagFetcherBridge.class))
  @IndexingDependency(
      derivedFrom = {@ObjectPath(@PropertyValue(propertyName = "reviews"))},
      reindexOnUpdate = SHALLOW
  )
  private IdListWrapper getTopTagMessages() {
    return new IdListWrapper(this.reviews.stream()
                                         .map(ric -> ric.getReview().getId())
                                         .toList());
  }

  public static Collection createCollection(Member member, String title,
                                            String description, String thumbnailUrlPath,
                                            List<Review> reviews) {

    Collection collection = new Collection();
    collection.member = member;
    collection.updateTitle(title);
    collection.updateDescription(description);
    collection.updateThumbnailUrlPath(thumbnailUrlPath);
    collection.updateReviews(reviews);
    return collection;
  }

  public void updateReviews(List<Review> reviews) {
    this.reviews.clear();
    this.reviews.addAll(reviews.stream().map(
        review -> ReviewInCollection.createReviewInCollection(this.member, this, review)).toList());
  }

  public void updateTitle(String title) {
    this.title = title;
  }

  public void updateDescription(String description) {
    // allow empty string
    this.description = description;
  }

  public void updateThumbnailUrlPath(String thumbnailUrlPath) {
    this.thumbnailUrlPath = thumbnailUrlPath;
  }

  public void toggleExposure(Boolean exposure) {
    this.isExposed = exposure;
  }
}
