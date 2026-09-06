package com.bamoe.bpmnGererator.service.aiservice;

import com.bamoe.bpmnGererator.ai.prompt.SystemPrompts;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiWorkflowGenerationServiceImpl
        implements AiWorkflowGenerationService {

    private final ChatClient chatClient;

    public AiWorkflowGenerationServiceImpl(ChatClient chatClient) {

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

    @Override
    public WorkflowResponse updateBpmn(String updatePromt){
        String systemPrompt = SystemPrompts.UPDATE_PROMPT ;
        WorkflowResponse workflow = chatClient.prompt()
                .system(systemPrompt)
                .user(updatePromt)
                .call()
                .entity(WorkflowResponse.class);
        return workflow ;
    }
}