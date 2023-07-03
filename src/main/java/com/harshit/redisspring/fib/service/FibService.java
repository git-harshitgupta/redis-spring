package com.harshit.redisspring.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FibService {

    //have a strategy for cache evict
    @Cacheable(value = "math:fib" , key = "#index")
    public int getFib(int index, String name){
        System.out.println("Calculating fib for "+index+" name "+name);
        return fib(index);
    }

    //whenever we do put post patch delete
    @CacheEvict(value = "math:fib" , key = "#index")
    public void clearCache(int index){
        System.out.println("clearing hash key");
    }

    @Scheduled(fixedRate = 60_000)
    @CacheEvict(value = "math:fib", allEntries = true)
    public void clearCache(){
        System.out.println("Clearing all cache");
    }

    private int fib(int index){
        if (index<2)
            return index;
        return fib(index-1)+fib(index-2);
    }



}
