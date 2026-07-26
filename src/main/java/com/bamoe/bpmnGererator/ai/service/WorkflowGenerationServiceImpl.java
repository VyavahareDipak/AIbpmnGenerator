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
              Your task is to convert a user's workflow description into a valid WorkflowResponse JSON.
              Supported Node Types
              - START_EVENT
              - END_EVENT
              - USER_TASK
              - SERVICE_TASK
              - SCRIPT_TASK
              - EXCLUSIVE_GATEWAY
              - PARALLEL_GATEWAY
              Do not generate unsupported node types.
              
              WORKFLOW RULES:
              1. Exactly one START_EVENT.
              2. At least one END_EVENT.
              3. Every node must have a unique id.
              4. Every connection must reference existing nodes.
              5. The workflow must be fully connected.
              6. No isolated or unreachable nodes.
              7. All business decisions must be represented using gateways.
              8. Declare every process variable before it is used.
              9. Variables used inside conditionExpression must exist in variables[].
              10. Loops are allowed but must always return through a gateway.
              11. Never connect multiple sequence flows directly into a USER_TASK.
              12. When user input is required, the USER_TASK should get input and expose outputs and the following gateway evaluates those values.
              
              START_EVENT RULES:
              - one outgoing flow.
              - No incoming flow.
              
              END_EVENT RULES:
              - One incoming flows.
              - No outgoing flow.
              
              USER_TASK RULES:
              Represents human work.
              Rules:
              - one incoming flow.
              - one outgoing flow.
              - Never perform branching.
              - Never evaluate conditions.
              - If user provides data, expose it as outputs.
              - Decision making always happens in the next gateway.
              If multiple paths reach a USER_TASK, insert a Converging Exclusive Gateway before it.
              
              SERVICE_TASK RULES:
              Represents automatic system execution.
              Rules
              - No human interaction.
              - Do not use for approvals.
              
              SCRIPT_TASK RULES:
              Represents automatic calculations or data transformations.
              Rules
              - No human interaction.
              - No workflow decisions.
              
              EXCLUSIVE_GATEWAY RULES:
              Always specify gatewayDirection :   
              Diverging Gateway:
              - One incoming.
              - Two or more outgoing.
              - Every outgoing flow must either:
                - contain conditionExpression
                - OR be defaultFlow=true.
              
              Converging Gateway: 
              - Two or more incoming.
              - Exactly one outgoing.
              
              Use Exclusive Gateway:
              - conditional execution
              
              PARALLEL_GATEWAY RULES:
              Fork
              - gatewayDirection = Diverging
              - One incoming.
              - Multiple outgoing.
              Join
              - gatewayDirection = Converging
              - Multiple incoming.
              - One outgoing.

              CONNECTION RULES:
              Normal Flow
              - No conditionExpression.
              Conditional Flow
              - Source must be a Diverging Exclusive Gateway.
              - Must contain conditionExpression or be defaultFlow.
              
              SEND BACK PATTERN:
              Never connect directly back into a USER_TASK.
              Correct Pattern
              
              Previous Task
                    ↓
              Converging Gateway
                    ↓
              User Task
                    ↓
              Diverging Gateway
                 ↙        ↘
              Next      Send Back
              The send-back path must return to the Converging Gateway.
              
              VALIDATION:
              Before returning JSON ensure:
              ✓ One START_EVENT
              ✓ At least one END_EVENT
              ✓ Unique ids
              ✓ Valid node references
              ✓ Connected workflow
              ✓ No USER_TASK has multiple incoming flows
              ✓ All gateways have valid gatewayDirection
              ✓ Diverging gateways contain conditions/default flow
              ✓ Converging gateways have exactly one outgoing flow
              ✓ All variables are declared before use
              
              Return ONLY WorkflowResponse JSON.
                """;

        WorkflowResponse workflow = chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .call()
                .entity(WorkflowResponse.class);
        return workflow ;
    }
}