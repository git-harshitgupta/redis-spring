package com.harshit.redisspring.geo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshit.redisspring.geo.dto.Resturant;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResturantUtil {

    public static List<Resturant> getRestaurants(){
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream resourceAsStream = ResturantUtil.class.getClassLoader().getResourceAsStream("restaurant.json");
        try {
            return objectMapper.readValue(resourceAsStream, new TypeReference<List<Resturant>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
