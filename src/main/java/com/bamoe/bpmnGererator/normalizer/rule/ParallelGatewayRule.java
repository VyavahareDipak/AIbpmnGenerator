package com.bamoe.bpmnGererator.normalizer.rule;

import com.bamoe.bpmnGererator.dto.ConnectionDto;
import com.bamoe.bpmnGererator.dto.NodeDto;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGenerator.normalizer.WorkflowNormalizationRule ;
import com.bamoe.bpmnGererator.normalizer.WorkflowUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParallelGatewayRule
        implements WorkflowNormalizationRule {

    @Override
    public void normalize(WorkflowResponse workflow) {

        List<NodeDto> gateways =
                WorkflowUtils.getNodesByType(
                        workflow,
                        "PARALLEL_GATEWAY");

        for (NodeDto gateway : gateways) {

            normalizeGatewayDirection(workflow, gateway);

            validateIncomingOutgoing(workflow, gateway);

            removeConditions(workflow, gateway);

        }

    }

    private void normalizeGatewayDirection(
            WorkflowResponse workflow,
            NodeDto gateway) {

        if (gateway.getGatewayDirection() != null &&
                !gateway.getGatewayDirection().isBlank()) {
            return;
        }

        int incoming =
                WorkflowUtils.incomingCount(workflow, gateway.getId());

        int outgoing =
                WorkflowUtils.outgoingCount(workflow, gateway.getId());

        if (incoming == 1 && outgoing > 1) {

            gateway.setGatewayDirection("Diverging");

        } else if (incoming > 1 && outgoing == 1) {

            gateway.setGatewayDirection("Converging");

        } else {

            throw new IllegalStateException(
                    "Cannot determine gatewayDirection for parallel gateway : "
                            + gateway.getId());

        }

    }

    private void validateIncomingOutgoing(
            WorkflowResponse workflow,
            NodeDto gateway) {

        int incoming =
                WorkflowUtils.incomingCount(workflow, gateway.getId());

        int outgoing =
                WorkflowUtils.outgoingCount(workflow, gateway.getId());

        if ("Diverging".equals(gateway.getGatewayDirection())) {

            if (incoming != 1) {

                throw new IllegalStateException(
                        "Parallel diverging gateway must have exactly one incoming flow : "
                                + gateway.getId());

            }

            if (outgoing < 2) {

                throw new IllegalStateException(
                        "Parallel diverging gateway must have at least two outgoing flows : "
                                + gateway.getId());

            }

        } else if ("Converging".equals(gateway.getGatewayDirection())) {

            if (incoming < 2) {

                throw new IllegalStateException(
                        "Parallel converging gateway must have at least two incoming flows : "
                                + gateway.getId());

            }

            if (outgoing != 1) {

                throw new IllegalStateException(
                        "Parallel converging gateway must have exactly one outgoing flow : "
                                + gateway.getId());

            }

        }

    }

    private void removeConditions(
            WorkflowResponse workflow,
            NodeDto gateway) {

        if (!"Diverging".equals(gateway.getGatewayDirection())) {
            return;
        }

        List<ConnectionDto> outgoing =
                WorkflowUtils.getOutgoingConnections(
                        workflow,
                        gateway.getId());

        for (ConnectionDto connection : outgoing) {

            connection.setConditionExpression(null);
            connection.setDefaultFlow(false);

        }

    }

}
