package com.meetsipdrink.roulette.service;

import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.roulette.dto.RouletteDto;
import com.meetsipdrink.roulette.entity.Roulette;
import com.meetsipdrink.roulette.repository.RouletteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RouletteService {
    private final RouletteRepository repository;

    public RouletteService(RouletteRepository repository) {
        this.repository = repository;
    }

    public Roulette createRoulette(Roulette roulette) {
        verifyExistsDrinkType(roulette.getDrinkType());
        Roulette savedRoulette = repository.save(roulette);
        return savedRoulette;
    }

    public Roulette updateRoulette(Roulette roulette, String drinkType) {
        Optional.ofNullable(drinkType)
                .ifPresent(value -> {roulette.setDrinkType(value);});
        return repository.save(roulette);
    }

    public List<Roulette> findAllRoulettes() {
        return repository.findAll();
    }

    public void deleteRoulette(String drinkType) {
        Roulette roulette = findVerifyRoulette(drinkType);
        repository.delete(roulette);
    }

    public void verifyExistsDrinkType(String drinkType) {
        Optional<Roulette> optionalRoulette = repository.findByDrinkType(drinkType);
        if (optionalRoulette.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ROULETTE_EXISTS);
        }
    }

    public Roulette findVerifyRoulette(String drinkType) {
        Optional<Roulette> optionalRoulette = repository.findByDrinkType(drinkType);
        Roulette findRoulette =
                optionalRoulette.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.ROULETTE_NOT_FOUND));
        return findRoulette;
    }
}
