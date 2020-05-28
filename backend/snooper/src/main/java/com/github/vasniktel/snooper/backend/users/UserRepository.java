package com.github.vasniktel.snooper.backend.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.HashSet;
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

  @Query(
      "from UserDto u where lower(u.name) like concat('%', lower(:query), '%') "
  )
  List<UserDto> search(String query);

  default List<UserDto> search(List<String> query) {
    var set = new HashSet<UserDto>();

    for (var item : query) {
      set.addAll(search(item));
    }

    return new ArrayList<>(set);
  }
}
