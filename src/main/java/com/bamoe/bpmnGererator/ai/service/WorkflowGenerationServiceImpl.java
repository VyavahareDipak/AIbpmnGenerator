package com.bamoe.bpmnGererator.ai.service;

import com.bamoe.bpmnGererator.ai.prompt.SystemPrompts;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class WorkflowGenerationServiceImpl
        implements WorkflowGenerationService {

    private final ChatClient chatClient;

    public WorkflowGenerationServiceImpl(ChatClient chatClient) {

        this.chatClient = chatClient;

    }

    @Override
    public WorkflowResponse generate(String prompt) {

        String systemPrompt = SystemPrompts.systemPrompt ;
        WorkflowResponse workflow = chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .call()
                .entity(WorkflowResponse.class);
        return workflow ;
    }
}