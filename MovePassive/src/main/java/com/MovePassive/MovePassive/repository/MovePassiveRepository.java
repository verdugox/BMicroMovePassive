package com.MovePassive.MovePassive.repository;

import com.MovePassive.MovePassive.entity.MovePassive;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MovePassiveRepository extends ReactiveMongoRepository<MovePassive, String> {

    Mono<MovePassive> findByIdentityAccount(String identityAccount);

}
