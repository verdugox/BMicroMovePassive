package com.MovePassive.MovePassive.web;


import com.MovePassive.MovePassive.entity.MovePassive;
import com.MovePassive.MovePassive.service.MovePassiveService;
import com.MovePassive.MovePassive.web.mapper.MovePassiveMapper;
import com.MovePassive.MovePassive.web.model.MovePassiveModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/movePassive")
public class MovePassiveController {

    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    private MovePassiveService movePassiveService;


    @Autowired
    private MovePassiveMapper movePassiveMapper;


    @GetMapping("/findAll")
    public Mono<ResponseEntity<Flux<MovePassiveModel>>> getAll(){
        log.info("getAll executed");
        return Mono.just(ResponseEntity.ok()
                .body(movePassiveService.findAll()
                        .map(movePassive -> movePassiveMapper.entityToModel(movePassive))));
    }

    @GetMapping("/findById/{id}")
    public Mono<ResponseEntity<MovePassiveModel>> findById(@PathVariable String id){
        log.info("findById executed {}", id);
        Mono<MovePassive> response = movePassiveService.findById(id);
        return response
                .map(productPassive -> movePassiveMapper.entityToModel(productPassive))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<MovePassiveModel>> create(@Valid @RequestBody MovePassiveModel request){
        log.info("create executed {}", request);
        return movePassiveService.create(movePassiveMapper.modelToEntity(request))
                .map(movePassive -> movePassiveMapper.entityToModel(movePassive))
                .flatMap(p -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "movePassive", p.getId())))
                        .body(p)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovePassiveModel>> updateById(@PathVariable String id, @Valid @RequestBody MovePassiveModel request){
        log.info("updateById executed {}:{}", id, request);
        return movePassiveService.update(id, movePassiveMapper.modelToEntity(request))
                .map(movePassive -> movePassiveMapper.entityToModel(movePassive))
                .flatMap(p -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "movePassive", p.getId())))
                        .body(p)))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id){
        log.info("deleteById executed {}", id);
        return movePassiveService.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
