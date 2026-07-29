package com.bamoe.bpmnGererator.serializer.diagram.model;

import com.bamoe.bpmnGererator.model.bpmn.NodePosition;

import java.util.LinkedHashMap;
import java.util.Map;

public class LayoutResult {

    private Map<String, NodePosition> nodePositions = new LinkedHashMap<>();

    private Map<String, EdgeRoute> edgeRoutes = new LinkedHashMap<>();

    public Map<String, NodePosition> getNodePositions() {
        return nodePositions;
    }

    public void setNodePositions(Map<String, NodePosition> nodePositions) {
        this.nodePositions = nodePositions;
    }

    public Map<String, EdgeRoute> getEdgeRoutes() {
        return edgeRoutes;
    }

    public void setEdgeRoutes(Map<String, EdgeRoute> edgeRoutes) {
        this.edgeRoutes = edgeRoutes;
    }
}