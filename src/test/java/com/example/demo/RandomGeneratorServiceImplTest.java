package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RandomGeneratorServiceImplTest {

    private RandomGeneratorServiceImpl randomGeneratorServiceImpl;

    @BeforeEach
    public void setup() {
        randomGeneratorServiceImpl = new RandomGeneratorServiceImpl();
    }
    
    @Test
    public void generateRandomFactorIsBetweenExpectedLimits() throws Exception {
        List<Integer> randomFactors = IntStream.range(0, 1000)
            .map(i -> randomGeneratorServiceImpl.generateRandomFactor())
            .boxed().collect(Collectors.toList());
        
        assertThat(randomFactors).containsOnlyElementsOf(
            IntStream.range(11, 100).boxed().collect(Collectors.toList())
        );
    }
}