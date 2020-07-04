package com.example.demo;


import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public final class User {
    
    private final String alias;

    protected User() {
        alias = null;
    }
}