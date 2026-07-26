package com.bamoe.bpmnGererator.model.bpmn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExclusiveGateway extends Gateway {
    private String defaultFlowId;
    private String gatewayDirection ;
}