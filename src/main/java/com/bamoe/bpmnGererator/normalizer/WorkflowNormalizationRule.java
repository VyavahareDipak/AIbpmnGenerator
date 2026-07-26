package com.bamoe.bpmnGenerator.normalizer;

import com.bamoe.bpmnGererator.dto.WorkflowResponse ;
public interface WorkflowNormalizationRule {

    void normalize(WorkflowResponse workflow);

}