package com.github.vasniktel.snooper.backend.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserDto, String> {
  @Query("select u from " +
      "UserDto u inner join SubscriptionDto s on u.id = s.followerId " +
      "where s.followeeId = :id")
  List<UserDto> getFollowersOf(String id);

  @Query("select u from " +
      "UserDto u inner join SubscriptionDto s on u.id = s.followeeId " +
      "where s.followerId = :id")
  List<UserDto> getFolloweesOf(String id);
}
