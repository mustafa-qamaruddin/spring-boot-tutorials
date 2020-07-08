package com.example.demo;


import java.util.List;


public interface MultiplicationService {
    Multiplication createRandomMultiplication();

    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

    List<MultiplicationResultAttempt> getStatsForUser(final String userAlias);

    MultiplicationResultAttempt getResultById(final Long resultId);
}