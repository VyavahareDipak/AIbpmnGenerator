package com.bamoe.bpmnGererator.model.bpmn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Definitions extends BpmnElement {

    private String targetNamespace;

    private Process process;

}