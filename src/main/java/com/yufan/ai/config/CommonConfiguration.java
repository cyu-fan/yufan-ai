package com.yufan.ai.config;

import com.yufan.ai.constants.SystemConstants;
import com.yufan.ai.tools.CourseTools;
import org.antlr.v4.runtime.atn.PredicateTransition;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;

import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Vector;

@Configuration
public class CommonConfiguration {
    @Bean
    public ChatClient chatClient(OllamaChatModel model,ChatMemory chatMemory){
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个很冷酷，又很傲娇的智能助手，你的名字叫喻繁，请以喻繁的语气回答问题")
                //环绕增强输出日志
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @Bean
    public ChatClient gameChatClient(OllamaChatModel model, ChatMemory chatMemory){
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROPERTY)
                //环绕增强输出日志
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @Bean
    public ChatClient serviceChatClient(OllamaChatModel model, ChatMemory chatMemory, CourseTools courseTools){
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.CUSTOMER_SERVICE_SYSTEM)
                //环绕增强输出日志
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools(courseTools)
                .build();
    }


    @Bean
    public ChatMemory chatMemory(){
        return new InMemoryChatMemory();
    }

    @Bean
    public VectorStore vectorStore(OllamaEmbeddingModel embeddingModel){
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    public ChatClient pdfChatClient(OllamaChatModel model, ChatMemory chatMemory, VectorStore vectorStore){
        return ChatClient
                .builder(model)
                .defaultSystem("请根据用户上传的文档上下文回答问题，遇到没有的，不要随意乱造")
                .defaultAdvisors(
                        //环绕增强输出日志
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(
                              vectorStore,
                              SearchRequest.builder()
                                      .similarityThreshold(0.6d)
                                      .topK(2)
                                      .build()
                        )
                )
                .build();
    }

    @Bean
    public ChatClient mediaClient(OllamaChatModel model,ChatMemory chatMemory){
        return ChatClient
                .builder(model)
                //.defaultOptions(ChatOptions.builder().model("qwen-omni-turbo").build())
                .defaultSystem("你是一个很冷酷，又很傲娇的智能助手，你的名字叫喻繁，请以喻繁的语气回答问题")
                //环绕增强输出日志
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }
}
