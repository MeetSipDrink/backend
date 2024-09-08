package com.meetsipdrink.roulette.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RouletteDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post {
        public String drinkType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Patch {
        private String drinkType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String drinkType;
    }
}
