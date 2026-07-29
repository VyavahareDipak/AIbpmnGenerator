package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.NodePosition;
import com.bamoe.bpmnGererator.model.bpmn.Process ;

import java.io.IOException;
import java.util.Map;

public interface LayoutEngine {

    Map<String, NodePosition> layout(Process process) ;

}