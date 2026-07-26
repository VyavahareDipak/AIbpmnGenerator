package com.bamoe.bpmnGererator.ai.service;

import com.bamoe.bpmnGererator.dto.WorkflowResponse;

public interface WorkflowGenerationService {

    WorkflowResponse generate(String prompt);
}
