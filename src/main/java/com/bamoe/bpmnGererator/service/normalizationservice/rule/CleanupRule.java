package com.bamoe.bpmnGererator.service.normalizationservice.rule;

import com.bamoe.bpmnGererator.dto.*;
import com.bamoe.bpmnGererator.util.WorkflowNormalizationUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CleanupRule implements WorkflowNormalizationRule {

    @Override
    public void normalize(WorkflowResponse workflow) {

        removeDuplicateConnections(workflow);

        removeSelfLoops(workflow);

        cleanupConnectionFields(workflow);

//        removeUnusedVariables(workflow);

        removeUnnecessaryGateways(workflow) ;

    }

    private void removeDuplicateConnections(WorkflowResponse workflow) {

        Set<String> seen = new HashSet<>();

        workflow.setConnections(

                workflow.getConnections()
                        .stream()
                        .filter(c -> {

                            String key =
                                    c.getSource() + "->" +
                                            c.getTarget() + "|" +
                                            Objects.toString(c.getConditionExpression(), "");

                            return seen.add(key);

                        })
                        .collect(Collectors.toList())

        );

    }

    private void removeSelfLoops(WorkflowResponse workflow) {

        workflow.setConnections(

                workflow.getConnections()
                        .stream()
                        .filter(c -> !Objects.equals(
                                c.getSource(),
                                c.getTarget()))
                        .collect(Collectors.toList())

        );

    }

    private void cleanupConnectionFields(WorkflowResponse workflow) {

        for (ConnectionDto connection : workflow.getConnections()) {

            if (connection.getName() != null) {

                connection.setName(connection.getName().trim());

                if (connection.getName().isEmpty()) {
                    connection.setName(null);
                }

            }

            if (connection.getConditionExpression() != null) {

                connection.setConditionExpression(
                        connection.getConditionExpression().trim());

                if (connection.getConditionExpression().isEmpty()) {
                    connection.setConditionExpression(null);
                }

            }

        }

    }

//    private void removeUnusedVariables(WorkflowResponse workflow) {
//        if (workflow.getVariables() == null) {
//            return;
//        }
//        Set<NodeVariableDto> usedVariables = new HashSet<>();
//
//        workflow.getConnections().forEach(connection -> {
//
//            if (connection.getConditionExpression() != null) {
//
//                for (VariableDto variable : workflow.getVariables()) {
//
//                    if (connection.getConditionExpression()
//                            .contains(variable.getName())) {
//
//                        usedVariables.add(new NodeVariableDto(variable.getName(),variable.getType()));
//
//                    }
//
//                }
//
//            }
//
//        });
//
//        workflow.getNodes().forEach(node -> {
//
//            if (node.getInputs() != null) {
//                usedVariables.addAll(node.getInputs());
//            }
//
//            if (node.getOutputs() != null) {
//                usedVariables.addAll(node.getOutputs());
//            }
//
//        });
//
//        workflow.setVariables(
//
//                workflow.getVariables()
//                        .stream()
//                        .filter(v -> usedVariables.contains(v.getName()))
//                        .collect(Collectors.toList())
//
//        );
//
//    }

    private void removeUnnecessaryGateways(WorkflowResponse workflow) {

        List<NodeDto> toRemove = new ArrayList<>();

        for (NodeDto node : workflow.getNodes()) {

            if (!"EXCLUSIVE_GATEWAY".equals(node.getType())
                    && !"PARALLEL_GATEWAY".equals(node.getType())) {
                continue;
            }

            List<ConnectionDto> incoming =
                    WorkflowNormalizationUtil.getIncomingConnections(workflow, node.getId());

            List<ConnectionDto> outgoing =
                    WorkflowNormalizationUtil.getOutgoingConnections(workflow, node.getId());

            // gateway does nothing
            if (incoming.size() == 1 && outgoing.size() == 1) {

                ConnectionDto in = incoming.get(0);
                ConnectionDto out = outgoing.get(0);

                ConnectionDto replacement = new ConnectionDto();

                replacement.setSource(in.getSource());
                replacement.setTarget(out.getTarget());

                // preserve condition if present
                if (out.getConditionExpression() != null
                        && !out.getConditionExpression().isBlank()) {

                    replacement.setConditionExpression(
                            out.getConditionExpression());

                    replacement.setDefaultFlow(out.isDefaultFlow());
                    replacement.setName(out.getName());

                } else {

                    replacement.setConditionExpression(
                            in.getConditionExpression());

                    replacement.setDefaultFlow(in.isDefaultFlow());
                    replacement.setName(in.getName());

                }

                workflow.getConnections().remove(in);
                workflow.getConnections().remove(out);
                workflow.getConnections().add(replacement);

                toRemove.add(node);
            }
        }

        workflow.getNodes().removeAll(toRemove);
    }

}
