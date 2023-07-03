package com.harshit.redisspring.city.client;

import com.harshit.redisspring.city.dto.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CityClient {

    private final WebClient webClient;

    public CityClient(@Value("${city.service.url}") String url){
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<City> getCity(final String zipcode){
        return webClient
                .get()
                .uri("{zipcode}",zipcode)
                .retrieve()
                .bodyToMono(City.class);
    }

    public Flux<City> getAll(){
        return webClient.get().retrieve().bodyToFlux(City.class);
    }

}
