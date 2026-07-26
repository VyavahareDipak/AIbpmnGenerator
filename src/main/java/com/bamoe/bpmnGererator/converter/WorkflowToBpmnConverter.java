package com.bamoe.bpmnGererator.converter;


import com.bamoe.bpmnGererator.dto.ConnectionDto;
import com.bamoe.bpmnGererator.dto.NodeDto;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.model.bpmn.*;
import com.bamoe.bpmnGererator.model.bpmn.Process;

import java.util.HashMap;
import java.util.Map;

public class WorkflowToBpmnConverter {

    private final NodeFactory nodeFactory = new NodeFactory();

    public Definitions convert(WorkflowResponse response) {

        Definitions definitions = new Definitions();
        definitions.setId("definitions");

        Process process = new Process();
        process.setId(response.getProcessId());
        process.setName(response.getProcessName());

        definitions.setProcess(process);

        Map<String, FlowNode> nodeMap = new HashMap<>();

        /*
         * Step 1
         * Create all BPMN nodes
         */
        for (NodeDto dto : response.getNodes()) {

            FlowNode node = nodeFactory.create(dto);

            process.getFlowElements().add(node);

            nodeMap.put(node.getId(), node);
        }

        /*
         * Step 2
         * Create sequence flows
         */
        int flowCounter = 1;

        for (ConnectionDto connection : response.getConnections()) {

            SequenceFlow flow = new SequenceFlow();

            flow.setId("Flow_" + flowCounter++);
            flow.setSourceRef(connection.getSource());
            flow.setTargetRef(connection.getTarget());

            flow.setName(connection.getName());
            flow.setConditionExpression(connection.getConditionExpression());
            flow.setDefaultFlow(connection.isDefaultFlow());

            process.getFlowElements().add(flow);

            FlowNode sourceNode = nodeMap.get(connection.getSource());
            FlowNode targetNode = nodeMap.get(connection.getTarget());

            if (sourceNode != null) {
                sourceNode.getOutgoing().add(flow.getId());
            }

            if (targetNode != null) {
                targetNode.getIncoming().add(flow.getId());
            }
        }

        for (FlowElement element : process.getFlowElements()) {

            if (!(element instanceof ExclusiveGateway gateway)) {
                continue;
            }

            for (FlowElement flowElement : process.getFlowElements()) {

                if (!(flowElement instanceof SequenceFlow flow)) {
                    continue;
                }

                if (flow.getSourceRef().equals(gateway.getId())
                        && flow.isDefaultFlow()) {

                    gateway.setDefaultFlowId(flow.getId());
                    break;
                }
            }
        }

        return definitions;
    }
}