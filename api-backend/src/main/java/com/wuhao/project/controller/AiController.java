package com.wuhao.project.controller;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private ClientV4 client;
    /**
     * sse调用
     */
    @PostMapping("/sse")
    public Flowable<ModelData> doStreamRequest(Float temperature) {
        List<ChatMessage> aiChatMessages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "使用Java代码写一个Hello World");
        aiChatMessages.add(chatMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .invokeMethod(Constants.invokeMethod)
                .temperature(temperature)
                .messages(aiChatMessages)
                .build();
        ModelApiResponse modelApiResponse = client.invokeModelApi(chatCompletionRequest);
        return modelApiResponse.getFlowable();
    }
}
