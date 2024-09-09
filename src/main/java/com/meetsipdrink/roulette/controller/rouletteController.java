package com.meetsipdrink.roulette.controller;

import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.roulette.dto.RouletteDto;
import com.meetsipdrink.roulette.entity.Roulette;
import com.meetsipdrink.roulette.mapper.RouletteMapper;
import com.meetsipdrink.roulette.service.RouletteService;
import com.meetsipdrink.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/roulette")
@Validated
@Slf4j
public class rouletteController {
    private final static String ROULETTE_DEFAULT_URL = "/roulette";
    private final RouletteMapper mapper;
    private final RouletteService service;

    public rouletteController(RouletteMapper mapper, RouletteService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity postRoulette(@Valid @RequestBody RouletteDto.Post requestBody) {
        Roulette roulette = mapper.roulettePostToRoulette(requestBody);

        Roulette createdRoulette = service.createRoulette(roulette);
        URI location = UriCreator.createUri(ROULETTE_DEFAULT_URL, createdRoulette.getRouletteId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{drink-type}")
    public ResponseEntity patchRoulette(@PathVariable("drink-type") String drinkType,
                                        @Valid @RequestBody RouletteDto.Patch requestBody) {
        Roulette findRoulette = service.findVerifyRoulette(drinkType);
        Roulette roulette =
                service.updateRoulette(findRoulette, requestBody.getDrinkType());

        return new ResponseEntity<>(
                new SingleResponseDto<>(roulette),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getRoulette() {
        List<Roulette> roulettes = service.findAllRoulettes();

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.rouletteToRouletteResponseList(roulettes)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{drink-type}")
    public ResponseEntity deleteRoulette(@PathVariable("drink-type") String drinkType) {
        service.deleteRoulette(drinkType);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
