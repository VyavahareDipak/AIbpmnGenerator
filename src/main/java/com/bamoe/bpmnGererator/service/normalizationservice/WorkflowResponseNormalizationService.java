package com.bamoe.bpmnGererator.service.normalizationservice;

import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.service.normalizationservice.rule.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowResponseNormalizationService {

    private final List<WorkflowNormalizationRule> rules = List.of(
            new StartEndRule(),

            new UserTaskRule(),

            new ExclusiveGatewayRule(),

            new ParallelGatewayRule(),

            new DecisionGatewayRule(),

//            new VariableRule(),

            new CleanupRule()


    );

    public WorkflowResponse normalize(WorkflowResponse workflow) {

        for (WorkflowNormalizationRule rule : rules) {
            rule.normalize(workflow);
        }

        return workflow;
    }
}
