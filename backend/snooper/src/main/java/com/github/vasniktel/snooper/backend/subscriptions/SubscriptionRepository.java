package com.github.vasniktel.snooper.backend.subscriptions;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SubscriptionRepository extends CrudRepository<SubscriptionDto, Integer> {
  SubscriptionDto getFirstByFollowerIdAndFolloweeId(String followerId, String followeeId);

  @Transactional
  void deleteByFollowerIdAndFolloweeId(String followerId, String followeeId);
}
