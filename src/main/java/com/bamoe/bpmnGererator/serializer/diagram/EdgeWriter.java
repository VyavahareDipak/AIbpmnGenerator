package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.SequenceFlow;
import com.bamoe.bpmnGererator.model.bpmn.NodePosition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

public class EdgeWriter {

    public void write(Document doc,
                      Element plane,
                      SequenceFlow flow,
                      Map<String, NodePosition> positions) {

        NodePosition source =
                positions.get(flow.getSourceRef());

        NodePosition target =
                positions.get(flow.getTargetRef());

        Element edge =
                doc.createElement("bpmndi:BPMNEdge");

        edge.setAttribute(
                "id",
                flow.getId() + "_edge");

        edge.setAttribute(
                "bpmnElement",
                flow.getId());

        plane.appendChild(edge);

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