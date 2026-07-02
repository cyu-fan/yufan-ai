package com.yufan.ai.controller;

import com.yufan.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class MediaController {
    private final ChatClient mediaClient;

    private final ChatHistoryRepository chatHistoryRepository;
    @RequestMapping(value = "/chat")
    public String chat(
            @RequestParam("prompt") String prompt,
            @RequestParam("chatId") String chatId,
            @RequestParam(value = "files",required = false) List<MultipartFile> files){
        //1.保存会话id
        chatHistoryRepository.save("chat",chatId);
        //2.请求模型
        if(files==null || files.isEmpty()){
            return textChat(prompt,chatId);
        }
        else{
            return multiModelChat(prompt,files,chatId);
        }
}

    private String multiModelChat(String prompt, List<MultipartFile> files, String chatId) {
        //1.保存会话id
        chatHistoryRepository.save("chat",chatId);
        //2.0 解析多模态
        List<Media> medias=files.stream().map(file->new Media(MimeType.valueOf(file.getContentType()),
                        file.getResource())
                )
                .toList();
        //2.请求模型
        return mediaClient.prompt()
                .user(p->p.text(prompt).media(medias.toArray(Media[]::new)))
                //a相当于AdvisorSpec类型的一个对象
                //给增强器传递动态参数
                .advisors(a->a.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .call()
                .content();
    }

    private String textChat(String prompt, String chatId) {
        //1.保存会话id
        chatHistoryRepository.save("chat",chatId);
        //2.请求模型
        return mediaClient.prompt()
                .user(prompt)
                //a相当于AdvisorSpec类型的一个对象
                //给增强器传递动态参数
                .advisors(a->a.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .call()
                .content();
    }
}
