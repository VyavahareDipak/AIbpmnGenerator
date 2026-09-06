package com.bamoe.bpmnGererator.service.normalizationservice.rule;

import com.bamoe.bpmnGererator.dto.ConnectionDto;
import com.bamoe.bpmnGererator.dto.NodeDto;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.util.WorkflowNormalizationUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExclusiveGatewayRule implements WorkflowNormalizationRule {

    @Override
    public void normalize(WorkflowResponse workflow) {

        List<NodeDto> gateways =
                WorkflowNormalizationUtil.getNodesByType(workflow, "EXCLUSIVE_GATEWAY");

        for (NodeDto gateway : gateways) {

            normalizeGatewayDirection(workflow, gateway);

            validateIncomingOutgoing(workflow, gateway);

            ensureDefaultFlow(workflow, gateway);

        }

    }

    /**
     * Infer gateway direction if not provided.
     */
    private void normalizeGatewayDirection(
            WorkflowResponse workflow,
            NodeDto gateway) {

        int incoming = WorkflowNormalizationUtil.incomingCount(workflow, gateway.getId());

        int outgoing = WorkflowNormalizationUtil.outgoingCount(workflow, gateway.getId());

        if (incoming > 1 && outgoing == 1) {
            gateway.setGatewayDirection("Converging");

        } else if (incoming == 1 && outgoing > 1) {
            gateway.setGatewayDirection("Diverging");
        } else {
            throw new IllegalStateException(
                    "Cannot determine gatewayDirection for gateway : "
                            + gateway.getId());
        }

    }

    /**
     * Validate gateway structure.
     */
    private void validateIncomingOutgoing(
            WorkflowResponse workflow,
            NodeDto gateway) {

        int incoming = WorkflowNormalizationUtil.incomingCount(workflow, gateway.getId());

        int outgoing = WorkflowNormalizationUtil.outgoingCount(workflow, gateway.getId());

        if ("Converging".equals(gateway.getGatewayDirection())) {

            if (outgoing != 1) {
                throw new IllegalStateException(
                        "Converging gateway must have exactly one outgoing flow : "
                                + gateway.getId());
            }
        }

        if ("Diverging".equals(gateway.getGatewayDirection())) {

            if (incoming != 1) {
                throw new IllegalStateException(
                        "Diverging gateway must have exactly one incoming flow : "
                                + gateway.getId());
            }

            if (outgoing < 2) {
                throw new IllegalStateException(
                        "Diverging gateway must have at least two outgoing flows : "
                                + gateway.getId());
            }
        }
    }

    /**
     * If no outgoing flow has condition/default,
     * make the first outgoing flow the default.
     */
    private void ensureDefaultFlow(
            WorkflowResponse workflow,
            NodeDto gateway) {

        if (!"Diverging".equals(gateway.getGatewayDirection())) {
            return;
        }

        List<ConnectionDto> outgoing =
                WorkflowNormalizationUtil.getOutgoingConnections(
                        workflow,
                        gateway.getId());

        boolean hasDefault =
                outgoing.stream()
                        .anyMatch(ConnectionDto::isDefaultFlow);

        if (hasDefault) {
            return;
        }

        boolean hasCondition =
                outgoing.stream()
                        .anyMatch(c ->
                                c.getConditionExpression() != null &&
                                        !c.getConditionExpression().isBlank());

        if (!hasCondition && !outgoing.isEmpty()) {

            outgoing.get(0).setDefaultFlow(true);

        }

    }
}
