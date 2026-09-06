package com.bamoe.bpmnGererator.serializer.diagram;


import com.bamoe.bpmnGererator.model.bpmn.*;
import  com.bamoe.bpmnGererator.model.bpmn.Process ;
import com.bamoe.bpmnGererator.serializer.diagram.model.LayoutResult;
import java.util.Map;

public class SimpleHorizontalLayoutEngine implements LayoutEngine {

    private static final int START_X = 220;
    private static final int START_Y = 220;

    private static final int H_GAP = 260;

    @Override
    public LayoutResult layout(Process process) {

        LayoutResult layoutResult = new LayoutResult() ;

        Map<String, NodePosition> map = layoutResult.getNodePositions();

        int x = START_X;

        for (FlowElement e : process.getFlowElements()) {

            if (!(e instanceof FlowNode node))
                continue;

            NodePosition p = new NodePosition();

            p.setNodeId(node.getId());

            p.setX(x);
            p.setY(220);

            if (node instanceof StartEvent || node instanceof EndEvent || node instanceof ParallelGateway || node instanceof  ExclusiveGateway ) {

                p.setWidth(60);
                p.setHeight(60);

            } else {

                p.setWidth(180);
                p.setHeight(100);

            }

            map.put(node.getId(), p);

            x += H_GAP;
        }
        layoutResult.setNodePositions(map);
        return layoutResult;
    }
}