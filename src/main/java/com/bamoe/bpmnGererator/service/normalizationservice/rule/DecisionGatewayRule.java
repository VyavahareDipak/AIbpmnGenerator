package com.bamoe.bpmnGererator.service.normalizationservice.rule;

import com.bamoe.bpmnGererator.dto.ConnectionDto;
import com.bamoe.bpmnGererator.dto.NodeDto;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.util.WorkflowNormalizationUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DecisionGatewayRule implements WorkflowNormalizationRule {

    @Override
    public void normalize(WorkflowResponse workflow) {

        List<NodeDto> gateways =
                WorkflowNormalizationUtil.getNodesByType(
                        workflow,
                        "EXCLUSIVE_GATEWAY");
        for (NodeDto gateway : gateways) {
            // Only Diverging gateways require conditions/default flow
            if (!"Diverging".equalsIgnoreCase(gateway.getGatewayDirection())) {
                continue;
            }
            List<ConnectionDto> outgoing =
                    WorkflowNormalizationUtil.getOutgoingConnections(
                            workflow,
                            gateway.getId());
            boolean defaultFound = false;

            // Find existing default flow
            for (ConnectionDto connection : outgoing) {

                if (connection.isDefaultFlow()) {
                    defaultFound = true;
                    break;
                }

            }

            // If no default flow exists, make the last one default
            if (!defaultFound && !outgoing.isEmpty()) {

                outgoing.get(outgoing.size() - 1)
                        .setDefaultFlow(true);

            }
            // Every non-default flow must have a condition
            for (ConnectionDto connection : outgoing) {

                if (connection.isDefaultFlow()) {
                    continue;
                }

                if (connection.getConditionExpression() == null
                        || connection.getConditionExpression().isBlank()) {
                        connection.setConditionExpression("false");
                }
            }

        }

    }
}
