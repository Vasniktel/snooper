package com.github.vasniktel.snooper.backend.users;

import com.github.vasniktel.snooper.backend.subscriptions.SubscriptionDto;
import com.github.vasniktel.snooper.backend.subscriptions.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserRepository userRepository;
  private final SubscriptionRepository subscriptionRepository;

  @Autowired
  public UserController(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
    this.userRepository = userRepository;
    this.subscriptionRepository = subscriptionRepository;
  }

  @PostMapping("/add")
  void addUser(@RequestBody UserDto user) {
    userRepository.save(user);
  }

  @GetMapping("/get")
  UserDto getUserById(@RequestParam("id") String id) {
    return userRepository.findById(id).orElse(null);
  }

  @GetMapping("/followers")
  List<UserDto> getFollowersOf(@RequestParam("id") String id) {
    return userRepository.getFollowersOf(id);
  }

  @GetMapping("/followees")
  List<UserDto> getFolloweesOf(@RequestParam("id") String id) {
    return userRepository.getFolloweesOf(id);
  }

  @GetMapping("/follow")
  void follow(
      @RequestParam("followerId") String followerId,
      @RequestParam("followeeId") String followeeId
  ) {
    var dto = new SubscriptionDto();
    dto.setFollowerId(followerId);
    dto.setFolloweeId(followeeId);
    subscriptionRepository.save(dto);
  }
}
