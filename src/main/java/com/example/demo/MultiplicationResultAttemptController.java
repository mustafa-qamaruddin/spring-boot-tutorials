package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


@RestController
@RequestMapping("/results")
final class MultiplicationResultAttemptController {
  
    private final MultiplicationService multiplicationService;

    @Autowired
    MultiplicationResultAttemptController(
        final MultiplicationService multiplicationService
    ) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    ResponseEntity<MultiplicationResultAttempt> postResult(
        @RequestBody final MultiplicationResultAttempt multiplicationResultAttempt) {

        final boolean isCorrect = multiplicationService.checkAttempt(multiplicationResultAttempt);
        final MultiplicationResultAttempt attemptCopy = new MultiplicationResultAttempt(
                multiplicationResultAttempt.getUser(), multiplicationResultAttempt.getMultiplication(),
                multiplicationResultAttempt.getResultAttempt(), isCorrect);
        return ResponseEntity.ok(attemptCopy);
    }

    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") final String alias) {
        return ResponseEntity.ok(
            multiplicationService.getStatsForUser(alias)
        );
    }

    @GetMapping("/{resultId}")
    ResponseEntity<MultiplicationResultAttempt> getResultById(final @PathVariable("resultId") Long resultId) {
        return ResponseEntity.ok(
            multiplicationService.getResultById(resultId)
        );
    }
}