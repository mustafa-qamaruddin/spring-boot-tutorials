package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/multiplications")
final class MultiplicationController {

    private final MultiplicationService multiplicationService;
    private final int serverPort;

    @Autowired
    public MultiplicationController(
        final MultiplicationService multiplicationService,
        @Value("${server.port}") final int serverPort
    ) {
        this.multiplicationService = multiplicationService;
        this.serverPort = serverPort;
    }
    
    @GetMapping("/random")
    Multiplication getRandoMultiplication() {
        log.info("Multiplication Server @ {}", serverPort);
        return multiplicationService.createRandomMultiplication();
    }
}