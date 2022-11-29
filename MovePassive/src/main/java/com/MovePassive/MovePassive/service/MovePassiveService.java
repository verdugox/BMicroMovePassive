package com.MovePassive.MovePassive.service;

import com.MovePassive.MovePassive.entity.MovePassive;
import com.MovePassive.MovePassive.repository.MovePassiveRepository;
import com.MovePassive.MovePassive.web.mapper.MovePassiveMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MovePassiveService {

    @Autowired
    private MovePassiveRepository movePassiveRepository;

    @Autowired
    private MovePassiveMapper movePassiveMapper;

    public Flux<MovePassive> findAll(){
        log.debug("findAll executed");
        return movePassiveRepository.findAll();
    }

    public Mono<MovePassive> findById(String movePassiveId){
        log.debug("findById executed {}" , movePassiveId);
        return movePassiveRepository.findById(movePassiveId);
    }

    public Mono<MovePassive> create(MovePassive movePassive){
        log.debug("create executed {}",movePassive);
        return movePassiveRepository.save(movePassive);
    }

    public Mono<MovePassive> update(String movePassiveId, MovePassive movePassive){
        log.debug("update executed {}:{}", movePassiveId, movePassive);
        return movePassiveRepository.findById(movePassiveId)
                .flatMap(dbMovePassive -> {
                    movePassiveMapper.update(dbMovePassive, movePassive);
                    return movePassiveRepository.save(dbMovePassive);
                });
    }

    public Mono<MovePassive>delete(String movePassiveId){
        log.debug("delete executed {}",movePassiveId);
        return movePassiveRepository.findById(movePassiveId)
                .flatMap(existingMovePassive -> movePassiveRepository.delete(existingMovePassive)
                        .then(Mono.just(existingMovePassive)));
    }

}
