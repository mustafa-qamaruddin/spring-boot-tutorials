package com.example.demo;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import java.util.List;


@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class MultiplicationResultAttempt {

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinColumn(name="USER_ID")
    private final List<User> user;

    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinColumn(name="MULTIPLICATION_ID")
    private final List<Multiplication> multiplication;

    
    private final int resultAttempt;
    private final boolean correct;


    // MultiplicationResultAttempt() {
    //     user = null;
    //     multiplication = null;
    //     resultAttempt = -1;
    //     correct = false;
    // }

}