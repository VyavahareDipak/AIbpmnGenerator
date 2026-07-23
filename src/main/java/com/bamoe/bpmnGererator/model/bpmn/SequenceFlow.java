package com.bamoe.bpmnGererator.model.bpmn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SequenceFlow extends FlowElement {

    private String sourceRef;

    private String targetRef;

}
