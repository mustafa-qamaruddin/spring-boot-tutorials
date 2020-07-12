package com.example.demo;


import javax.transaction.Transactional;

import com.example.demo.event.EventDispatcher;
import com.example.demo.event.MultiplicationSolvedEvent;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private final RandomGeneratorService randomGeneratorService;
    private final MultiplicationResultAttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
            final MultiplicationResultAttemptRepository attemptRepository, final UserRepository userRepository,
            final EventDispatcher eventDispatcher ) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        final int factorA = randomGeneratorService.generateRandomFactor();
        final int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt resultAttempt) {

        log.info("{}", resultAttempt);

        final Optional<User> user = userRepository.findByAlias(resultAttempt.getUser().getAlias());

        final boolean correct = resultAttempt.getResultAttempt() == resultAttempt.getMultiplication().getFactorA()
                * resultAttempt.getMultiplication().getFactorB();

        Assert.isTrue(!resultAttempt.isCorrect(), "You can't send an attempt marked as correct");

        final MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
                user.orElse(resultAttempt.getUser()), resultAttempt.getMultiplication(),
                resultAttempt.getResultAttempt(), correct);

        attemptRepository.save(checkedAttempt);

        log.info(
            "{} {} {}",
            checkedAttempt.getId(), checkedAttempt.getUser().getId(), checkedAttempt.isCorrect()
        );

        eventDispatcher.send(
            new MultiplicationSolvedEvent(
                checkedAttempt.getId(), checkedAttempt.getUser().getId(), checkedAttempt.isCorrect()
            )
        );

        return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(final String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }

    @Override
    public MultiplicationResultAttempt getResultById(final Long resultId) {
        return attemptRepository.findById(resultId).orElse(null);
    }
}