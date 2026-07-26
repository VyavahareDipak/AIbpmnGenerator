package com.bamoe.bpmnGererator.ai.service;

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

        String systemPrompt = """
                You are an expert BPMN workflow designer for BAMOE 9.       
                    Convert the user's workflow description into a valid WorkflowResponse JSON.
                    
                    Return ONLY valid JSON.
                    Do not return markdown.
                    Do not explain anything.
                
                    SUPPORTED NODE TYPES:
                    - START_EVENT
                    - END_EVENT
                    - USER_TASK
                    - SERVICE_TASK
                    - SCRIPT_TASK
                    - EXCLUSIVE_GATEWAY
                    - PARALLEL_GATEWAY    
                    Do not generate any other node types.

                    WORKFLOW RULES:
                    1. Create a workflow that accurately represents the user's business process.
                    2. Use meaningful ids and names.
                    3. Use USER_TASK for human activities.
                    4. Use SERVICE_TASK for automatic system work.
                    5. Use SCRIPT_TASK only for calculations or data transformation.
                    6. Use EXCLUSIVE_GATEWAY for business decisions.
                    7. Use PARALLEL_GATEWAY only when activities execute simultaneously.
                    8. If a decision depends on user input, create a process variable and reference it in the gateway condition.
                    9. If a USER_TASK collects values, expose them as outputs.
                    10. Use conditionExpression only on conditional branches.
                    11. Prefer simple workflows.
                    12. Do not introduce unnecessary gateways or tasks.
                    13. Only create Parallel Gateways when the user explicitly describes parallel execution.
                    14. Do not insert placeholder, dummy, or artificial tasks.
                    15.Do not introduce nodes that are not explicitly required by the business process.
                    16.Never create dummy tasks, placeholder tasks, helper tasks, or artificial gateways merely to satisfy workflow structure.
                    17.Generate the simplest workflow that correctly models the user's business requirements.
                    18.Use PARALLEL_GATEWAY only if the user explicitly describes parallel execution, concurrent work, or multiple activities that should happen simultaneously. 
                    19.Never use a Parallel Gateway simply to satisfy workflow structure.
                    20.If a USER_TASK is immediately followed by an EXCLUSIVE_GATEWAY, every variable referenced by that gateway's conditionExpression. must be exposed asinput and output variable of the USER_TASK. Same varible should define as process variable as well.
                  
                    APPROVAL PATTERN
                    Approval decisions happen AFTER the approving User Task.
                    
                    Example
                    
                    Maker
                    ↓
                    
                    Checker
                    ↓
                    
                    Exclusive Gateway
                    
                    Approve -> Continue
                    
                    Reject -> Send Back
                    
                    ==========================
                    SEND BACK PATTERN
                    ==========================
                    
                    If a task is sent back, return to the previous User Task through a gateway instead of connecting directly whenever possible.
                    
                    ==========================
                    VARIABLES
                    ==========================
                    
                    Declare every process variable used by conditionExpression.
                    
                    Example
                    
                    approved : boolean
                    
                    decision : string
                    
                    status : string
                    
                    ==========================
                    OUTPUT
                    ==========================
                    
                    Return exactly one WorkflowResponse JSON object.
                """;

        WorkflowResponse workflow = chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .call()
                .entity(WorkflowResponse.class);
        return workflow ;
    }
}