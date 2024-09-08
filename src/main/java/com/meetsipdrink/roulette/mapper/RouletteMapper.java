package com.meetsipdrink.roulette.mapper;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.meetsipdrink.roulette.dto.RouletteDto;
import com.meetsipdrink.roulette.entity.Roulette;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouletteMapper {
    Roulette roulettePostToRoulette(RouletteDto.Post requestBody);
    Roulette roulettePatchToRoulette(RouletteDto.Patch requestBody);
    RouletteDto.Response rouletteToRouletteResponse(Roulette roulette);
    List<RouletteDto.Response> rouletteToRouletteResponseList(List<Roulette> rouletteList);
}
