package com.github.vasniktel.snooper.backend.subscriptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
  private final SubscriptionRepository repository;

  @Autowired
  public SubscriptionController(SubscriptionRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/add")
  void add(
      @RequestParam("followerId") String followerId,
      @RequestParam("followeeId") String followeeId
  ) {
    var dto = new SubscriptionDto();
    dto.setFollowerId(followerId);
    dto.setFolloweeId(followeeId);
    repository.save(dto);
  }

  @GetMapping("/remove")
  void remove(
      @RequestParam("followerId") String followerId,
      @RequestParam("followeeId") String followeeId
  ) {
    repository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
  }

  @GetMapping("/isFollowee")
  boolean isFollowee(
      @RequestParam("followerId") String followerId,
      @RequestParam("followeeId") String followeeId
  ) {
    return repository.getFirstByFollowerIdAndFolloweeId(followerId, followeeId) != null;
  }
}
