package com.meetsipdrink.roulette.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "roulette")
@Getter
@Setter
@NoArgsConstructor
public class Roulette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rouletteId;

    @Column(nullable = false, unique = true)
    private String drinkType;
}
