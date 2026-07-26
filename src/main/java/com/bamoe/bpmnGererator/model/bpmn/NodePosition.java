package com.bamoe.bpmnGererator.model.bpmn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodePosition {

    private String nodeId;

    private int x;
    private int y;

    private int width;
    private int height;

}
