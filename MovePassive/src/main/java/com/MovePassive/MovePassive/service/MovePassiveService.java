package com.MovePassive.MovePassive.service;

import com.MovePassive.MovePassive.entity.MovePassive;
import com.MovePassive.MovePassive.entity.ProductPassive;
import com.MovePassive.MovePassive.repository.MovePassiveRepository;
import com.MovePassive.MovePassive.web.mapper.MovePassiveMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MovePassiveService {

    @Autowired
    private MovePassiveRepository movePassiveRepository;

    @Autowired
    private MovePassiveMapper movePassiveMapper;


    private final String BASE_URL = "http://localhost:8084";

    TcpClient tcpClient = TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .doOnConnected(connection ->
                    connection.addHandlerLast(new ReadTimeoutHandler(3))
                            .addHandlerLast(new WriteTimeoutHandler(3)));
    private final WebClient client = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))  // timeout
            .build();

    public Mono<ProductPassive> findProductPassiveByAccount(String identityAccount){
        return this.client.get().uri("/v1/productPassive/findByIdentityAccount/{identityAccount}",identityAccount)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(ProductPassive.class);
    }

    public Mono<ProductPassive> updateProductPassiveById(ProductPassive productPassive){
        return this.client.put().uri("/v1/productPassive/{id}",productPassive.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(productPassive), ProductPassive.class)
                .retrieve()
                .bodyToMono(ProductPassive.class);
    }

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
        return findProductPassiveByAccount(movePassive.getIdentityAccount())
                .flatMap(productPassive1 -> {
                    if(movePassive.getOperationType().equals("ABONO")){
                        productPassive1.setAvailableAmount(productPassive1.getAvailableAmount() + movePassive.getAmount());
                    }
                    else{
                        productPassive1.setAvailableAmount(productPassive1.getAvailableAmount() - movePassive.getAmount());
                    }
                    log.debug("productpassive" +productPassive1.getId());
                    updateProductPassiveById(productPassive1).subscribe();
                    return movePassiveRepository.save(movePassive);
                });
    }

    public Mono<MovePassive> update(String movePassiveId, MovePassive movePassive){
        log.debug("update executed {}:{}", movePassiveId, movePassive);
        return movePassiveRepository.findById(movePassiveId)
                .flatMap(dbMovePassive -> {
                    movePassiveMapper.update(dbMovePassive, movePassive);
                    return movePassiveRepository.save(dbMovePassive);
                });
    }


    public Mono<ProductPassive> updateProductPassiveWebClient(ProductPassive productPassive){
        return updateProductPassiveById(productPassive);
    }

    public Mono<MovePassive>delete(String movePassiveId){
        log.debug("delete executed {}",movePassiveId);
        return movePassiveRepository.findById(movePassiveId)
                .flatMap(existingMovePassive -> movePassiveRepository.delete(existingMovePassive)
                        .then(Mono.just(existingMovePassive)));
    }

}
