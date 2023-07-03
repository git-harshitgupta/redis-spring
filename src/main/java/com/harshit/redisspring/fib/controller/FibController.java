package com.harshit.redisspring.fib.controller;

import com.harshit.redisspring.fib.service.FibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("fib")
public class FibController {

    @Autowired
    private FibService fibService;

    @GetMapping("{index}/{name}")
    public Mono<Integer> getFib(@PathVariable int index, @PathVariable String name){
        return Mono.fromSupplier(()->fibService.getFib(index,name));
    }

    //real life it will post patch delete
    @GetMapping("{index}/clear")
    public Mono<Void> clearCache(@PathVariable int index){
        return Mono.fromRunnable(()->fibService.clearCache(index));
    }

}
