package com.bamoe.bpmnGererator.model.bpmn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParallelGateway extends Gateway {
    private String gatewayDirection ;
}
