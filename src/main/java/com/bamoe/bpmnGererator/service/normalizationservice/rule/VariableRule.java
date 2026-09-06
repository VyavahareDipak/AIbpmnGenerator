//package com.bamoe.bpmnGererator.normalizer.rule;
//
//import com.bamoe.bpmnGererator.dto.*;
//import com.bamoe.bpmnGenerator.normalizer.WorkflowNormalizationRule ;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Component
//public class VariableRule implements WorkflowNormalizationRule {
//
//    private static final Pattern VARIABLE_PATTERN =
//            Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*\\b");
//
//    @Override
//    public void normalize(WorkflowResponse workflow) {
//
//        if (workflow.getVariables() == null) {
//            workflow.setVariables(new ArrayList<>());
//        }
//
//        Map<String, VariableDto> variables = new LinkedHashMap<>();
//
//        // Existing variables
//        for (VariableDto variable : workflow.getVariables()) {
//            variables.put(variable.getName(), variable);
//        }
//
//        // Variables from user task inputs / outputs
//        for (NodeDto node : workflow.getNodes()) {
//
//            if (!"USER_TASK".equals(node.getType())) {
//                continue;
//            }
//
//            if (node.getInputs() != null) {
//                for (NodeVariableDto input : node.getInputs()) {
//                    variables.putIfAbsent(
//                            input.getVariable(),
//                            new VariableDto(input.getVariable(), "string"));
//                }
//            }
//
//            if (node.getOutputs() != null) {
//                for (NodeVariableDto input : node.getOutputs()) {
//                    variables.putIfAbsent(
//                            input.getVariable(),
//                            new VariableDto(input.getVariable(), "string"));
//                }
//            }
//
//        }
//
//        // Variables from condition expressions
//        for (ConnectionDto connection : workflow.getConnections()) {
//
//            String expression = connection.getConditionExpression();
//
//            if (expression == null || expression.isBlank()) {
//                continue;
//            }
//
//            Matcher matcher = VARIABLE_PATTERN.matcher(expression);
//
//            while (matcher.find()) {
//
//                String token = matcher.group();
//
//                if (isKeyword(token)) {
//                    continue;
//                }
//
//                variables.putIfAbsent(
//                        token,
//                        new VariableDto(token, "string"));
//
//            }
//
//        }
//
//        workflow.setVariables(new ArrayList<>(variables.values()));
//    }
//
//    private boolean isKeyword(String token) {
//
//        return switch (token) {
//            case "true",
//                    "false",
//                    "null",
//                    "and",
//                    "or",
//                    "not" -> true;
//            default -> false;
//        };
//
//    }
//
//}
