package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.NodePosition;
import com.bamoe.bpmnGererator.model.bpmn.Process ;
import com.bamoe.bpmnGererator.serializer.diagram.model.LayoutResult;

import java.io.IOException;
import java.util.Map;

public interface LayoutEngine {

    public LayoutResult layout(Process process) ;

}