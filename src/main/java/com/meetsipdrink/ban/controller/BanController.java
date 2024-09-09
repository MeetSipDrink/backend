package com.meetsipdrink.ban.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.ban.service.BanService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ban")
@RequiredArgsConstructor
public class BanController {

    private final BanService banService;

    @PostMapping
    public ResponseEntity<BanDto.Response> addBan(@Valid @RequestBody BanDto.Request requestBody) {
        BanDto.Response response = banService.addBan(requestBody);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<List<BanDto.Response>> getBanList(@PathVariable long memberId) {
        List<BanDto.Response> banList = banService.getBanList(memberId);
        return new ResponseEntity<>(banList, HttpStatus.OK);
    }
    @DeleteMapping("/{banId}")
    public ResponseEntity<Void> cancelBan(@PathVariable long banId) {
        banService.cancelBan(banId);
        return ResponseEntity.noContent().build();
    }

}
