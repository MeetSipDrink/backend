package com.meetsipdrink.roulette.repository;

import com.meetsipdrink.roulette.entity.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouletteRepository extends JpaRepository<Roulette, Long> {
    Optional<Roulette> findByDrinkType(String drinkType);
}
