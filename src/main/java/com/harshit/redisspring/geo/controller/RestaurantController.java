package com.harshit.redisspring.geo.controller;

import com.harshit.redisspring.geo.dto.Resturant;
import com.harshit.redisspring.geo.service.RestaurantLocatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("geo")
public class RestaurantController {

    @Autowired
    private RestaurantLocatorService restaurantLocatorService;

    @GetMapping("{zip}")
    public Flux<Resturant> getRestaurant(@PathVariable String zip){
        return restaurantLocatorService.getRestaurants(zip);
    }


}
