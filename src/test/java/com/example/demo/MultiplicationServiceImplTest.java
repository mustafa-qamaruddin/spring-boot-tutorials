package com.example.demo;

import org.junit.jupiter.api.Test;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import java.util.Optional;

import com.example.demo.event.EventDispatcher;
import com.example.demo.event.MultiplicationSolvedEvent;

import java.util.List;


public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(
            randomGeneratorService,
            attemptRepository,
            userRepository,
            eventDispatcher
        );
    }

    @Test
    public void createRandomMultiplicationTest() {
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        final Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        final Multiplication multiplication = new Multiplication(50, 60);
        final User user = new User("john_doe");
        final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
        final MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000,
                true);
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(
            attempt.getId(), attempt.getUser().getId(), true
        );

        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        final boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void checkWrongAttemptTest() {
        final Multiplication multiplication = new Multiplication(50, 60);
        final User user = new User("john_doe");
        final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);

        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        final boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(
            attempt.getId(),
            attempt.getUser().getId(),
            false
        );

        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void retrieveStatsTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051, false);
        List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe")).willReturn(latestAttempts);
        List<MultiplicationResultAttempt> latestAttemptsResult = multiplicationServiceImpl.getStatsForUser("john_doe");
        assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
    }
}