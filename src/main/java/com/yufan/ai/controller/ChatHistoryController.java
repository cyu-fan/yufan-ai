package com.yufan.ai.controller;

import com.yufan.ai.entity.vo.MessageVo;
import com.yufan.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/history")
public class ChatHistoryController {
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatMemory chatMemory;

    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type){
        return chatHistoryRepository.getChatIds(type);
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVo> getChatIds(@PathVariable("type") String type, @PathVariable("chatId") String chatId){
        List<Message> messages =chatMemory.get(chatId,10);
        if(messages==null){
            return List.of();
        }
        return messages.stream().map(MessageVo::new).toList();
    }
}
