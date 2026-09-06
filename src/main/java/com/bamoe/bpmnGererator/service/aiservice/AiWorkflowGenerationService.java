package com.bamoe.bpmnGererator.service.aiservice;

import com.bamoe.bpmnGererator.dto.WorkflowResponse;

public interface AiWorkflowGenerationService {

    WorkflowResponse generate(String prompt);

    public WorkflowResponse updateBpmn(String updatePromt);
}
