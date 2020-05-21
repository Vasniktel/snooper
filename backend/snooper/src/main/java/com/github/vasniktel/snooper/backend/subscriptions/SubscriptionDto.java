package com.github.vasniktel.snooper.backend.subscriptions;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
public class SubscriptionDto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "follower_id")
  @NonNull
  private String followerId;

  @Column(name = "followee_id")
  @NonNull
  private String followeeId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @NonNull
  public String getFollowerId() {
    return followerId;
  }

  public void setFollowerId(@NonNull String followerId) {
    this.followerId = followerId;
  }

  @NonNull
  public String getFolloweeId() {
    return followeeId;
  }

  public void setFolloweeId(@NonNull String followeeId) {
    this.followeeId = followeeId;
  }
}
