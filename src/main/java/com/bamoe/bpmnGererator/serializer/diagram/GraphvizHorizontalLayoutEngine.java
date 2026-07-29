package com.bamoe.bpmnGererator.serializer.diagram;

import com.bamoe.bpmnGererator.model.bpmn.*;
import com.bamoe.bpmnGererator.model.bpmn.Process;
import com.bamoe.bpmnGererator.serializer.diagram.model.EdgeRoute;
import com.bamoe.bpmnGererator.serializer.diagram.model.LayoutResult;
import com.bamoe.bpmnGererator.serializer.diagram.model.RoutePoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class GraphvizHorizontalLayoutEngine implements LayoutEngine {

    private static final int SCALE = 72;

    @Override
    public LayoutResult layout(Process process) {

        String dot = buildDot(process);

        String plain = executeGraphviz(dot);

        return parsePlain(process, plain);
    }

    private String buildDot(Process process) {

        StringBuilder sb = new StringBuilder();

        sb.append("digraph BPMN {\n");

        sb.append("graph [\n");
        sb.append("    rankdir=LR,\n");
        sb.append("    splines=ortho,\n");
        sb.append("    nodesep=0.8,\n");
        sb.append("    ranksep=1.3\n");
        sb.append("];\n");

        sb.append("node [\n");
        sb.append("    fontname=\"Arial\",\n");
        sb.append("    margin=0,\n");
        sb.append("    fixedsize=true\n");
        sb.append("];\n");


        for (FlowElement e : process.getFlowElements()) {

            if (!(e instanceof FlowNode node))
                continue;

            sb.append(node.getId())
                    .append(" [shape=")
                    .append(getShape(node))
                    .append("];\n");

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

    private String executeGraphviz(String dot) {

        try {

            ProcessBuilder pb =
                    new ProcessBuilder("dot", "-Tplain");

            java.lang.Process p = pb.start();

            p.getOutputStream().write(dot.getBytes());
            p.getOutputStream().close();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    p.getInputStream()));

            StringBuilder result = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                result.append(line).append("\n");

            }

            p.waitFor();

            return result.toString();

        } catch (IOException | InterruptedException e) {

            throw new RuntimeException(e);

        }
    }

    private LayoutResult parsePlain(
            Process process,
            String plain) {

        LayoutResult result = new LayoutResult();

        Map<String, NodePosition> nodes =
                result.getNodePositions();

        Map<String, EdgeRoute> routes =
                result.getEdgeRoutes();

        String[] lines = plain.split("\\R");

        double graphHeight = 0;

        for (String line : lines) {

            if (line.startsWith("graph")) {

                String[] p = line.split("\\s+");

                graphHeight =
                        Double.parseDouble(p[3]);

            }

        }

        for (String line : lines) {

            String[] parts = line.split("\\s+");

            if (parts.length == 0)
                continue;

            if ("node".equals(parts[0])) {

                String id = parts[1];

                double x =
                        Double.parseDouble(parts[2]);

                double y =
                        Double.parseDouble(parts[3]);

                NodePosition pos = new NodePosition();

                pos.setNodeId(id);

//                pos.setX((int) (x * SCALE));
//
//                pos.setY((int) ((graphHeight - y) * SCALE));

                FlowNode node = findNode(process, id);

                int width;
                int height;

                if (node instanceof StartEvent
                        || node instanceof EndEvent
                        || node instanceof ExclusiveGateway
                        || node instanceof ParallelGateway) {

                    width = 60;
                    height = 60;

                } else {

                    width = 180;
                    height = 100;
                }
                pos.setWidth(width);
                pos.setHeight(height);

                pos.setX((int)(x * SCALE - width / 2));
                pos.setY((int)((graphHeight - y) * SCALE - height / 2));

                nodes.put(id, pos);
            }

            if ("edge".equals(parts[0])) {

                EdgeRoute route = new EdgeRoute();

                route.setSourceId(parts[1]);
                route.setTargetId(parts[2]);

                int count =
                        Integer.parseInt(parts[3]);

                int index = 4;

                for (int i = 0; i < count; i++) {

                    double x =
                            Double.parseDouble(parts[index++]);

                    double y =
                            Double.parseDouble(parts[index++]);

                    route.getPoints().add(
                            new RoutePoint(
                                    (int) (x * SCALE),
                                    (int) ((graphHeight - y) * SCALE)
                            ));
                }

                routes.put(
                        route.getSourceId() + "_"
                                + route.getTargetId(),
                        route);

            }

        }

        return result;
    }

    private FlowNode findNode(
            Process process,
            String id) {

        for (FlowElement e : process.getFlowElements()) {

            if (e instanceof FlowNode node &&
                    node.getId().equals(id)) {

                return node;

            }

        }

        throw new IllegalArgumentException(id);
    }

    private String getShape(FlowNode node) {

        if (node instanceof StartEvent)
            return "circle";

        if (node instanceof EndEvent)
            return "doublecircle";

        if (node instanceof ExclusiveGateway)
            return "diamond, width=0.85,height=0.85,fixedsize=true";

        if (node instanceof ParallelGateway)
            return "diamond";

        return "box,width=2.5,height=1.4,fixedsize=true";
    }

}