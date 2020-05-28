package com.github.vasniktel.snooper.backend.messages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface MessageRepository extends CrudRepository<MessageDto, Integer> {
  List<MessageDto> findAllByOwnerIdOrderByDateDesc(String ownerId);

  @Query(
      "from MessageDto where owner.id = :id or owner.id in (" +
          "select s.followeeId from SubscriptionDto s " +
          "where s.followerId = :id) " +
          "order by date desc"
  )
  List<MessageDto> getUserFeed(String id);

  @Query(
      "from MessageDto m where lower(m.address) like concat('%',lower(:query),'%')"
  )
  List<MessageDto> search(String query);

  default List<MessageDto> search(List<String> query) {
    var set = new HashSet<MessageDto>();

    for (var item : query) {
      set.addAll(search(item));
    }

    return new ArrayList<>(set);
  }
}
