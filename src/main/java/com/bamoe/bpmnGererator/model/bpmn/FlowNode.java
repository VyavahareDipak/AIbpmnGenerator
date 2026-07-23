package com.bamoe.bpmnGererator.model.bpmn;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class FlowNode extends FlowElement {

    private List<String> incoming = new ArrayList<>();

    private List<String> outgoing = new ArrayList<>();

}