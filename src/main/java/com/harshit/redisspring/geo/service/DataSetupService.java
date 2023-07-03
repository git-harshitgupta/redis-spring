package com.harshit.redisspring.geo.service;

import com.harshit.redisspring.geo.dto.GeoLocation;
import com.harshit.redisspring.geo.dto.Resturant;
import com.harshit.redisspring.geo.util.ResturantUtil;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DataSetupService implements CommandLineRunner {
    private RGeoReactive<Resturant> geo;
    private RMapReactive<String, GeoLocation> map;

    @Autowired
    private RedissonReactiveClient client;

    @Override
    public void run(String... args) throws Exception {
        geo = client.getGeo("restaurants", new TypedJsonJacksonCodec(Resturant.class));
        map = client.getMap("us:texas", new TypedJsonJacksonCodec(String.class,GeoLocation.class));
        Flux.fromIterable(ResturantUtil.getRestaurants())
                .flatMap(r -> geo.add(r.getLongitude(), r.getLatitude(), r).thenReturn(r))
                .flatMap(r -> map.fastPut(r.getZip(),new GeoLocation(r.getLongitude(), r.getLatitude())))
                .doFinally(s -> System.out.println("restaurants added "+s))
                .subscribe();
    }
}
