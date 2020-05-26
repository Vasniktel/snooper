package com.github.vasniktel.snooper.backend.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserRepository repository;

  @Autowired
  public UserController(UserRepository repository) {
    this.repository = repository;
  }

  @PostMapping("/add")
  void addUser(@RequestBody UserDto user) {
    repository.save(user);
  }

  @GetMapping("/get")
  UserDto getUserById(@RequestParam("id") String id) {
    return repository.findById(id).orElse(null);
  }

  @GetMapping("/followers")
  List<UserDto> getFollowersOf(@RequestParam("id") String id) {
    return repository.getFollowersOf(id);
  }

  @GetMapping("/followees")
  List<UserDto> getFolloweesOf(@RequestParam("id") String id) {
    return repository.getFolloweesOf(id);
  }
}
