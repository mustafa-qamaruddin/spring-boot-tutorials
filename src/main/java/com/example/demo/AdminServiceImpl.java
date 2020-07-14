package com.example.demo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class AdminServiceImpl implements AdminService {
    
    private UserRepository userRepository;
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;
    private MultiplicationRepository multiplicationRepository;

    public AdminServiceImpl(
        final UserRepository userRepository,
        final MultiplicationRepository multiplicationRepository,
        final MultiplicationResultAttemptRepository multiplicationResultAttemptRepository
    ) {
        this.userRepository = userRepository;
        this.multiplicationRepository = multiplicationRepository;
        this.multiplicationResultAttemptRepository = multiplicationResultAttemptRepository;
    }

    @Override
    public void deleteDatabaseContents() {
        this.userRepository.deleteAll();
        this.multiplicationRepository.deleteAll();
        this.multiplicationResultAttemptRepository.deleteAll();
    }

}