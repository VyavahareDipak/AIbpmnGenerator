package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.NodePosition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ShapeWriter {

    public void write(Document doc,
                      Element plane,
                      NodePosition position) {

        Element shape =
                doc.createElement("bpmndi:BPMNShape");

        shape.setAttribute(
                "id",
                position.getNodeId() + "_shape");

        shape.setAttribute(
                "bpmnElement",
                position.getNodeId());

        plane.appendChild(shape);

        Element bounds =
                doc.createElement("dc:Bounds");

        bounds.setAttribute(
                "x",
                String.valueOf(position.getX()));

        bounds.setAttribute(
                "y",
                String.valueOf(position.getY()));

        bounds.setAttribute(
                "width",
                String.valueOf(position.getWidth()));

        bounds.setAttribute(
                "height",
                String.valueOf(position.getHeight()));

        shape.appendChild(bounds);

    }

}