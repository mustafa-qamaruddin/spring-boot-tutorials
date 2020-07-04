package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;


@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

    @Autowired
    public RandomGeneratorServiceImpl() {

    }

    final static int MINI_FAC = 11;
    final static int MAXI_FAC = 99;

    @Override
    public int generateRandomFactor() {
        return new Random().nextInt(
            (MAXI_FAC - MINI_FAC) + 1
        ) + MINI_FAC;
    }
    
}