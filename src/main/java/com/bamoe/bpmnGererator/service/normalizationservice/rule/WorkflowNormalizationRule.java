package com.bamoe.bpmnGererator.service.normalizationservice.rule;

import com.bamoe.bpmnGererator.dto.WorkflowResponse ;
public interface WorkflowNormalizationRule {

    void normalize(WorkflowResponse workflow);

}