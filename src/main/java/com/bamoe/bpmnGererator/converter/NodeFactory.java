package com.bamoe.bpmnGererator.converter;

import com.bamoe.bpmnGererator.dto.NodeDto;
import com.bamoe.bpmnGererator.model.bpmn.EndEvent;
import com.bamoe.bpmnGererator.model.bpmn.FlowNode;
import com.bamoe.bpmnGererator.model.bpmn.StartEvent;
import com.bamoe.bpmnGererator.model.bpmn.*;

public class NodeFactory {

    public FlowNode create(NodeDto nodeDto) {

        FlowNode node;

        switch (nodeDto.getType().toUpperCase()) {

            case "START_EVENT":
                node = new StartEvent();
                break;

            case "END_EVENT":
                node = new EndEvent();
                break;

            case "USER_TASK":
                node = new UserTask();

                break;

            case "SERVICE_TASK":
                node = new ServiceTask();

                break;

            case "SCRIPT_TASK":
                node = new ScriptTask();
                break;

            case "EXCLUSIVE_GATEWAY":
                node = new ExclusiveGateway();
                break;

            case "PARALLEL_GATEWAY":
                node = new ParallelGateway();
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported node type : " + nodeDto.getType());
        }

        node.setId(nodeDto.getId());
        node.setName(nodeDto.getName());

        return node;
    }
}
