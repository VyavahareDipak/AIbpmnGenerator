package com.bamoe.bpmnGererator.model.bpmn;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Process extends BpmnElement {

    private String name;

    private boolean executable = true;

    private List<FlowElement> flowElements = new ArrayList<>();

}