package com.bamoe.bpmnGererator.dto.request;

import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowUpdateLLMRequestDto {
    private String userPrompt;

    private WorkflowResponse workflow;
}
