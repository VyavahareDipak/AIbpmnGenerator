package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.*;


import com.bamoe.bpmnGererator.model.bpmn.Process;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.Map;

public class DiagramWriter {

    private final LayoutEngine layoutEngine =
            new GraphvizHorizontalLayoutEngine();

    private final ShapeWriter shapeWriter =
            new ShapeWriter();

    private final EdgeWriter edgeWriter =
            new EdgeWriter();

    public void write(Document doc,
                      Element definitions,
                      Process process)  {

        Element diagram =
                doc.createElement("bpmndi:BPMNDiagram");

        diagram.setAttribute(
                "id",
                process.getId() + "_diagram");

        definitions.appendChild(diagram);

        Element plane =
                doc.createElement("bpmndi:BPMNPlane");

        plane.setAttribute(
                "id",
                process.getId() + "_plane");

        plane.setAttribute(
                "bpmnElement",
                process.getId());

        diagram.appendChild(plane);

        Map<String, NodePosition> positions =
                layoutEngine.layout(process);

        for (NodePosition position : positions.values()) {
            shapeWriter.write(doc, plane, position);
        }

        for (FlowElement e : process.getFlowElements()) {

            if (e instanceof SequenceFlow flow) {
                edgeWriter.write(doc, plane, flow, positions);
            }

        }

    }

}