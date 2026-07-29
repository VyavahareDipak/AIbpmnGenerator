package com.bamoe.bpmnGererator.serializer.diagram.model;

import java.util.ArrayList;
import java.util.List;

public class EdgeRoute {

    private String sourceId;

    private String targetId;

    private List<RoutePoint> points = new ArrayList<>();

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public List<RoutePoint> getPoints() {
        return points;
    }

    public void setPoints(List<RoutePoint> points) {
        this.points = points;
    }
}