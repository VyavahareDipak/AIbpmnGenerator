package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.SequenceFlow;
import com.bamoe.bpmnGererator.model.bpmn.NodePosition;
import com.bamoe.bpmnGererator.serializer.diagram.model.EdgeRoute;
import com.bamoe.bpmnGererator.serializer.diagram.model.RoutePoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

import com.bamoe.bpmnGererator.model.bpmn.NodePosition;
import com.bamoe.bpmnGererator.model.bpmn.SequenceFlow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

public class EdgeWriter {

    public void write(Document doc,
                      Element plane,
                      SequenceFlow flow,
                      EdgeRoute route,
                      Map<String, NodePosition> positions) {

        Element edge =
                doc.createElement("bpmndi:BPMNEdge");

        edge.setAttribute(
                "id",
                flow.getId() + "_edge");

        edge.setAttribute(
                "bpmnElement",
                flow.getId());

        plane.appendChild(edge);

        // -----------------------------
        // Use Graphviz routing if present
        // -----------------------------
        if (route != null && !route.getPoints().isEmpty()) {

            for (RoutePoint point : route.getPoints()) {

                Element waypoint =
                        doc.createElement("di:waypoint");

                waypoint.setAttribute(
                        "x",
                        String.valueOf(point.getX()));

                waypoint.setAttribute(
                        "y",
                        String.valueOf(point.getY()));

                edge.appendChild(waypoint);
            }

            return;
        }

        // -----------------------------
        // Fallback: Straight line
        // -----------------------------
        NodePosition source =
                positions.get(flow.getSourceRef());

        NodePosition target =
                positions.get(flow.getTargetRef());

        Element w1 =
                doc.createElement("di:waypoint");

        w1.setAttribute(
                "x",
                String.valueOf(source.getX() + source.getWidth()));

        w1.setAttribute(
                "y",
                String.valueOf(source.getY() + source.getHeight() / 2));

        edge.appendChild(w1);

        Element w2 =
                doc.createElement("di:waypoint");

        w2.setAttribute(
                "x",
                String.valueOf(target.getX()));

        w2.setAttribute(
                "y",
                String.valueOf(target.getY() + target.getHeight() / 2));

        edge.appendChild(w2);
    }
}