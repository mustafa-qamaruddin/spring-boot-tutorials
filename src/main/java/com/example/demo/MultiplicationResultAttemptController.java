package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.example.demo.MultiplicationResultAttemptController.ResultResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @RequiredArgsConstructor
    @NoArgsConstructor(force=true)
    @Getter
    static final class ResultResponse {
        private final boolean correct;
    }

    @PostMapping
    ResponseEntity<ResultResponse> postResult(
        @RequestBody MultiplicationResultAttempt multiplicationResultAttempt
    ) {
        return ResponseEntity.ok(
            new ResultResponse(
                multiplicationService.checkAttempt(
                    multiplicationResultAttempt
                )
            )
        );
    }
}