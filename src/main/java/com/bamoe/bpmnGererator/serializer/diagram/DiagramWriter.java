package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.*;
import com.bamoe.bpmnGererator.model.bpmn.Process;
import com.bamoe.bpmnGererator.serializer.diagram.model.EdgeRoute;
import com.bamoe.bpmnGererator.serializer.diagram.model.LayoutResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DiagramWriter {

    private final LayoutEngine layoutEngine =
            new GraphvizHorizontalLayoutEngine();

    private final ShapeWriter shapeWriter =
            new ShapeWriter();

    private final EdgeWriter edgeWriter =
            new EdgeWriter();

    public void write(Document doc,
                      Element definitions,
                      Process process) {

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

        LayoutResult layoutResult =
                layoutEngine.layout(process);

        // Draw all BPMN Shapes
        for (NodePosition position :
                layoutResult.getNodePositions().values()) {

            shapeWriter.write(doc, plane, position);

        }

        // Draw all BPMN Edges
        for (FlowElement e : process.getFlowElements()) {

            if (!(e instanceof SequenceFlow flow)) {
                continue;
            }

            EdgeRoute route =
                    layoutResult.getEdgeRoutes().get(
                            flow.getSourceRef() + "_" + flow.getTargetRef());

            edgeWriter.write(
                    doc,
                    plane,
                    flow,
                    route,
                    layoutResult.getNodePositions());

        }

    }

}