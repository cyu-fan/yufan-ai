package com.yufan.ai.controller;

import com.yufan.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class CustomerServiceController {
    private final ChatClient serviceChatClient;

    private final ChatHistoryRepository chatHistoryRepository;
    @RequestMapping(value = "/service")
    public String chat(String prompt,String chatId){
        //1.保存会话id
        chatHistoryRepository.save("service",chatId);
        //2.请求模型
        return serviceChatClient.prompt()
                .user(prompt)
                //a相当于AdvisorSpec类型的一个对象
                //给增强器传递动态参数
                .advisors(a->a.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .call()
                .content();
    }
}
