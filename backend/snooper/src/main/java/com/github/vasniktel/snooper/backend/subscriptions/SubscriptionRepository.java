package com.github.vasniktel.snooper.backend.subscriptions;

import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<SubscriptionDto, Integer> {
}
