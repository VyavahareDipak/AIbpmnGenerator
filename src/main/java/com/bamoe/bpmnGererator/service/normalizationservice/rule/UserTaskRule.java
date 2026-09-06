package com.bamoe.bpmnGererator.service.normalizationservice.rule;

import com.bamoe.bpmnGererator.dto.ConnectionDto;
import com.bamoe.bpmnGererator.dto.NodeDto;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.util.WorkflowNormalizationUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserTaskRule implements WorkflowNormalizationRule {

    @Override
    public void normalize(WorkflowResponse workflow) {

        List<NodeDto> userTasks =
                WorkflowNormalizationUtil.getNodesByType(workflow, "USER_TASK");

        // Copy to avoid ConcurrentModificationException
        for (NodeDto userTask : new ArrayList<>(userTasks)) {

            List<ConnectionDto> incoming =
                    WorkflowNormalizationUtil.getIncomingConnections(
                            workflow,
                            userTask.getId());

            if (incoming.size() <= 1) {
                continue;
            }

            insertMergeGateway(
                    workflow,
                    userTask,
                    incoming);
        }
    }

    private void insertMergeGateway(
            WorkflowResponse workflow,
            NodeDto userTask,
            List<ConnectionDto> incomingConnections) {

        //---------------------------------------
        // Create Converging Gateway
        //---------------------------------------

        NodeDto gateway = new NodeDto();

        gateway.setId(WorkflowNormalizationUtil.generateGatewayId());
        gateway.setName("Merge Before " + userTask.getName());
        gateway.setType("EXCLUSIVE_GATEWAY");
        gateway.setGatewayDirection("Converging");

        WorkflowNormalizationUtil.addNode(workflow, gateway);

        //---------------------------------------
        // Redirect all incoming flows
        //
        // A -> UserTask
        //
        // becomes
        //
        // A -> Gateway
        //---------------------------------------

        for (ConnectionDto connection : incomingConnections) {
            connection.setTarget(gateway.getId());
        }

        //---------------------------------------
        // Add Gateway -> UserTask
        //---------------------------------------

        ConnectionDto gatewayToTask =
                WorkflowNormalizationUtil.createConnection(
                        gateway.getId(),
                        userTask.getId());

        WorkflowNormalizationUtil.addConnection(
                workflow,
                gatewayToTask);
    }

}