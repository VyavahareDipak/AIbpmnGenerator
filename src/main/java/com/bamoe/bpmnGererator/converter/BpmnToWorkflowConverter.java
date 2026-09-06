package com.bamoe.bpmnGererator.converter;


import com.bamoe.bpmnGererator.dto.*;

import org.springframework.stereotype.Component;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class BpmnToWorkflowConverter {

    public WorkflowResponse convert(String xml) {

        try {

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            factory.setNamespaceAware(true);

            Document document =
                    factory.newDocumentBuilder()
                            .parse(
                                    new ByteArrayInputStream(
                                            xml.getBytes(StandardCharsets.UTF_8)));

            document.getDocumentElement().normalize();

            WorkflowResponse response =
                    new WorkflowResponse();

            List<NodeDto> nodes =
                    new ArrayList<>();

            List<ConnectionDto> connections =
                    new ArrayList<>();

            List<VariableDto> variables =
                    new ArrayList<>();

            Element process =
                    (Element) document
                            .getElementsByTagNameNS(
                                    "*",
                                    "process")
                            .item(0);

            response.setProcessId(
                    process.getAttribute("id"));

            response.setProcessName(
                    process.getAttribute("name"));

            parseNodes(process, nodes);

            parseSequenceFlows(process, connections);

            extractVariables(nodes, variables);

            response.setNodes(nodes);
            response.setConnections(connections);
            response.setVariables(variables);

            return response;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse BPMN",
                    e);

        }
    }

    private void parseNodes(
            Element process,
            List<NodeDto> nodes) {

        NodeList children =
                process.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {

            Node node =
                    children.item(i);

            if (!(node instanceof Element element)) {
                continue;
            }

            String localName =
                    element.getLocalName();

            NodeDto dto =
                    createNode(element, localName);

            if (dto != null) {
                nodes.add(dto);
            }
        }
    }

    private NodeDto createNode(
            Element element,
            String localName) {

        String type =
                switch (localName) {

                    case "startEvent" ->
                            "START_EVENT";

                    case "endEvent" ->
                            "END_EVENT";

                    case "userTask" ->
                            "USER_TASK";

                    case "serviceTask" ->
                            "SERVICE_TASK";

                    case "exclusiveGateway" ->
                            "EXCLUSIVE_GATEWAY";

                    case "parallelGateway" ->
                            "PARALLEL_GATEWAY";

                    default -> null;
                };

        if (type == null) {
            return null;
        }

        NodeDto dto =
                new NodeDto();

        dto.setId(
                element.getAttribute("id"));

        dto.setName(
                element.getAttribute("name"));

        dto.setType(type);

        if ("exclusiveGateway".equals(localName)
                || "parallelGateway".equals(localName)) {

            String direction =
                    element.getAttribute(
                            "gatewayDirection");

            dto.setGatewayDirection(
                    direction);
        }

        dto.setInputs(extractInputVariables(element));
        dto.setOutputs(extractOutputVariables(element));

        return dto;
    }

    private void parseSequenceFlows(
            Element process,
            List<ConnectionDto> connections) {

        NodeList flows =
                process.getElementsByTagNameNS(
                        "*",
                        "sequenceFlow");

        for (int i = 0; i < flows.getLength(); i++) {

            Element flow =
                    (Element) flows.item(i);

            ConnectionDto dto =
                    new ConnectionDto();

            dto.setSource(
                    flow.getAttribute("sourceRef"));

            dto.setTarget(
                    flow.getAttribute("targetRef"));

            dto.setName(
                    flow.getAttribute("name"));

            String condition =
                    extractCondition(flow);

            dto.setConditionExpression(
                    condition);

            dto.setDefaultFlow(
                    false);

            connections.add(dto);
        }
    }

    private String extractCondition(
            Element sequenceFlow) {

        NodeList conditions =
                sequenceFlow.getElementsByTagNameNS(
                        "*",
                        "conditionExpression");

        if (conditions.getLength() == 0) {
            return null;
        }

        return conditions
                .item(0)
                .getTextContent();
    }

    private void extractVariables(
            List<NodeDto> nodes,
            List<VariableDto> variables) {

        Set<String> names =
                new HashSet<>();

        for (NodeDto node : nodes) {

            if (node.getInputs() != null) {

                for (NodeVariableDto v :
                        node.getInputs()) {

                    if (names.add(v.getVariable())) {

                        variables.add(
                                new VariableDto(
                                        v.getVariable(),
                                        v.getType()));
                    }
                }
            }

            if (node.getOutputs() != null) {

                for (NodeVariableDto v :
                        node.getOutputs()) {

                    if (names.add(v.getVariable())) {

                        variables.add(
                                new VariableDto(
                                        v.getVariable(),
                                        v.getType()));
                    }
                }
            }
        }
    }

    private List<NodeVariableDto> extractInputVariables(
            Element taskElement) {

        List<NodeVariableDto> variables =
                new ArrayList<>();

        NodeList associations =
                taskElement.getElementsByTagNameNS(
                        "*",
                        "dataInputAssociation");

        for (int i = 0; i < associations.getLength(); i++) {

            Element association =
                    (Element) associations.item(i);

            NodeList sources =
                    association.getElementsByTagNameNS(
                            "*",
                            "sourceRef");

            for (int j = 0; j < sources.getLength(); j++) {

                String variableName =
                        sources.item(j)
                                .getTextContent()
                                .trim();

                NodeVariableDto dto =
                        new NodeVariableDto();

                dto.setVariable(variableName);
                dto.setType("string");

                variables.add(dto);
            }
        }

        return variables;
    }

    private List<NodeVariableDto> extractOutputVariables(
            Element taskElement) {

        List<NodeVariableDto> variables =
                new ArrayList<>();

        NodeList associations =
                taskElement.getElementsByTagNameNS(
                        "*",
                        "dataOutputAssociation");

        for (int i = 0; i < associations.getLength(); i++) {

            Element association =
                    (Element) associations.item(i);

            NodeList targets =
                    association.getElementsByTagNameNS(
                            "*",
                            "targetRef");

            for (int j = 0; j < targets.getLength(); j++) {

                String variableName =
                        targets.item(j)
                                .getTextContent()
                                .trim();

                NodeVariableDto dto =
                        new NodeVariableDto();

                dto.setVariable(variableName);
                dto.setType("string");

                variables.add(dto);
            }
        }

        return variables;
    }
}
