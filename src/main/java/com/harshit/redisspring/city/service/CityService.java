package com.harshit.redisspring.city.service;

import com.harshit.redisspring.city.client.CityClient;
import com.harshit.redisspring.city.dto.City;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityClient cityClient;
    private RMapCacheReactive<String, City> cityMap;

    public CityService(RedissonReactiveClient client){
        this.cityMap = client.getMapCache("city", new TypedJsonJacksonCodec(String.class,City.class));
    }
    /*
        get from cache
        if empty - get from db / source
        put it in cache
        return
     */
    public Mono<City> getCity(final String zipcode){
        return this.cityMap.get(zipcode)
                .switchIfEmpty(
                        cityClient.getCity(zipcode)
                                .flatMap(c-> this.cityMap.fastPut(zipcode,c,10, TimeUnit.SECONDS).thenReturn(c))
                );
    }

    @Scheduled(fixedRate = 10_000)
    public void updateCity(){
        System.out.println("Updating zipcode");
        cityClient.getAll()
                .collectList()
                .map(list -> list.stream().collect(Collectors.toMap(City::getZip, Function.identity())))
                .flatMap(this.cityMap::putAll)
                .subscribe();
    }

}
