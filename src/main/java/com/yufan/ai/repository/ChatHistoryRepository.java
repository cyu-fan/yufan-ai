package com.yufan.ai.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

public interface ChatHistoryRepository {
    /**
     * 保存会话记录
     *
     * @param type   业务类型 chat,service,pdf
     * @param chatId 会话ID
     */
    void save(String type, String chatId);

    /**
     * 获取会话ID列表
     * @param type
     * @return 会话ID列表
     */
    List<String> getChatIds(String type);

}
