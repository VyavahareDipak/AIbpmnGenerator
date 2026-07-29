package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.*;
import com.bamoe.bpmnGererator.model.bpmn.Process;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.File;

public class GraphvizHorizontalLayoutEngine implements LayoutEngine {

    @Override
    public Map<String, NodePosition> layout(Process process) {

        String dot = buildDot(process);

        String plain =
                Graphviz.fromString(dot)
                        .render(Format.PLAIN)
                        .toString();
        System.out.println(plain);
        try {
            Graphviz.fromString(dot)
                    .render(Format.SVG)
                    .toFile(new File("graph.svg"));
        }catch (Exception e){
            e.printStackTrace();
        }

        return parsePlainOutput(process, plain);
    }

    private String buildDot(Process process) {

        StringBuilder sb = new StringBuilder();

        sb.append("digraph BPMN {\n");
        sb.append("rankdir=LR;\n");
        sb.append("nodesep=0.8;\n");
        sb.append("ranksep=1.2;\n");
        sb.append("splines=ortho;\n");

        for (FlowElement e : process.getFlowElements()) {

            if (!(e instanceof FlowNode node))
                continue;

            sb.append(node.getId())
                    .append(" [shape=box];\n");

        }

        for (FlowElement e : process.getFlowElements()) {

            if (!(e instanceof SequenceFlow flow))
                continue;

            sb.append(flow.getSourceRef())
                    .append(" -> ")
                    .append(flow.getTargetRef())
                    .append(";\n");

        }

        sb.append("}");

        return sb.toString();
    }

    private Map<String, NodePosition> parsePlainOutput(
            Process process,
            String plain) {

        Map<String, NodePosition> map = new LinkedHashMap<>();

        String[] lines = plain.split("\\R");

        for (String line : lines) {

            if (!line.startsWith("node"))
                continue;

            String[] parts = line.split("\\s+");

            String id = parts[1];

            double x = Double.parseDouble(parts[2]);

            double y = Double.parseDouble(parts[3]);

            NodePosition p = new NodePosition();

            p.setNodeId(id);

            p.setX((int) (x * 72));

            p.setY((int) (800 - y * 72));

            FlowNode node = findNode(process, id);

            if (node instanceof StartEvent
                    || node instanceof EndEvent
                    || node instanceof ExclusiveGateway
                    || node instanceof ParallelGateway) {

                p.setWidth(60);
                p.setHeight(60);

            } else {

                p.setWidth(180);
                p.setHeight(100);

            }

            map.put(id, p);

        }

        return map;
    }

    private FlowNode findNode(Process process, String id) {

        for (FlowElement e : process.getFlowElements()) {

            if (e instanceof FlowNode node &&
                    node.getId().equals(id)) {

                return node;

            }

        }

        throw new IllegalArgumentException(id);
    }

}