package com.meetsipdrink.bot.controller;

import com.meetsipdrink.bot.dto.ChatGPTRequest;
import com.meetsipdrink.bot.dto.ChatGPTResponse;
import com.meetsipdrink.bot.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class CustomBotController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api-url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    private final ChatGPTService service;

    public CustomBotController(ChatGPTService service) {
        this.service = service;
    }

    @GetMapping("/drink-recommend")
    public String chat(@RequestBody String prompt){
        String drinkPrompt = service.drinkPrompt(prompt);
        ChatGPTRequest request = new ChatGPTRequest(model, drinkPrompt);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
