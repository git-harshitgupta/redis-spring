package com.harshit.redisspring.geo.service;

import com.harshit.redisspring.geo.dto.GeoLocation;
import com.harshit.redisspring.geo.dto.Resturant;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Service
public class RestaurantLocatorService {

    private RGeoReactive<Resturant> geo;
    private RMapReactive<String, GeoLocation> map;


    public RestaurantLocatorService(RedissonReactiveClient client){
        geo = client.getGeo("restaurants", new TypedJsonJacksonCodec(Resturant.class));
        map = client.getMap("us:texas", new TypedJsonJacksonCodec(String.class,GeoLocation.class));

    }

    public Flux<Resturant> getRestaurants(final String zipcode){
        return map.get(zipcode)
                .map(gl ->GeoSearchArgs.from(gl.getLongitude(),
                        gl.getLatitude()).radius(5, GeoUnit.KILOMETERS))
                .flatMap(search->geo.search(search))
                .flatMapIterable(Function.identity());
    }

}
