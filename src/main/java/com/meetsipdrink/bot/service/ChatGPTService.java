package com.meetsipdrink.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatGPTService {
    public String drinkPrompt(String prompt) {
        return
                "이 문장이 끝난 뒤에 내용 : 안에 있는 단어나 문장에 대하여 공감하는 분위기로 술을 추천해주고 " +
                        "그 술에 대하여 자세히 설명해주면 좋겠고 " +
                        "특정 문맥이 없는 내용도 그와 관련하여 추천해주고 " +
                        "내 명령에 대해 설명하지말고 " +
                        "존댓말로 이야기 해줘 내용 :" + prompt;
    }
}
