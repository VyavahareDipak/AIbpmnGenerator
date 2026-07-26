package com.bamoe.bpmnGererator.normalizer;

import com.bamoe.bpmnGererator.dto.ConnectionDto;
import com.bamoe.bpmnGererator.dto.NodeDto;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.model.bpmn.FlowElement;
import com.bamoe.bpmnGererator.model.bpmn.Process;
import com.bamoe.bpmnGererator.model.bpmn.SequenceFlow;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WorkflowUtils {
    private WorkflowUtils() {
    }

    private static final AtomicInteger GATEWAY_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger FLOW_COUNTER = new AtomicInteger(1);

    /**
     * Find node by id.
     */
    public static Optional<NodeDto> findNodeById(
            WorkflowResponse workflow,
            String nodeId) {

        return workflow.getNodes()
                .stream()
                .filter(n -> n.getId().equals(nodeId))
                .findFirst();
    }

    /**
     * Incoming connections of a node.
     */
    public static List<ConnectionDto> getIncomingConnections(
            WorkflowResponse workflow,
            String nodeId) {

        return workflow.getConnections()
                .stream()
                .filter(c -> nodeId.equals(c.getTarget()))
                .collect(Collectors.toList());
    }

    /**
     * Outgoing connections of a node.
     */
    public static List<ConnectionDto> getOutgoingConnections(
            WorkflowResponse workflow,
            String nodeId) {

        return workflow.getConnections()
                .stream()
                .filter(c -> nodeId.equals(c.getSource()))
                .collect(Collectors.toList());
    }

    /**
     * Incoming connection count.
     */
    public static int incomingCount(
            WorkflowResponse workflow,
            String nodeId) {

        return getIncomingConnections(workflow, nodeId).size();
    }

    /**
     * Outgoing connection count.
     */
    public static int outgoingCount(
            WorkflowResponse workflow,
            String nodeId) {

        return getOutgoingConnections(workflow, nodeId).size();
    }

    /**
     * Add a connection.
     */
    public static void addConnection(
            WorkflowResponse workflow,
            ConnectionDto connection) {

        workflow.getConnections().add(connection);
    }

    /**
     * Remove a connection.
     */
    public static void removeConnection(
            WorkflowResponse workflow,
            ConnectionDto connection) {

        workflow.getConnections().remove(connection);
    }

    /**
     * Remove all connections.
     */
    public static void removeConnections(
            WorkflowResponse workflow,
            Collection<ConnectionDto> connections) {

        workflow.getConnections().removeAll(connections);
    }

    /**
     * Add node.
     */
    public static void addNode(
            WorkflowResponse workflow,
            NodeDto node) {

        workflow.getNodes().add(node);
    }

    /**
     * Remove node.
     */
    public static void removeNode(
            WorkflowResponse workflow,
            NodeDto node) {

        workflow.getNodes().remove(node);
    }

    /**
     * Generate gateway id.
     */
    public static String generateGatewayId() {
        return "gateway_" + GATEWAY_COUNTER.getAndIncrement();
    }

    /**
     * Generate sequence flow id.
     */
    public static String generateFlowId() {
        return "Flow_" + FLOW_COUNTER.getAndIncrement();
    }

    /**
     * Create a simple connection.
     */
    public static ConnectionDto createConnection(
            String source,
            String target) {

        ConnectionDto connection = new ConnectionDto();

//        connection.setId(generateFlowId());
        connection.setSource(source);
        connection.setTarget(target);
        connection.setName(source + "_to_" + target);

        return connection;
    }

    /**
     * Check whether node exists.
     */
    public static boolean nodeExists(
            WorkflowResponse workflow,
            String nodeId) {

        return findNodeById(workflow, nodeId).isPresent();
    }

    /**
     * Return all nodes of a type.
     */
    public static List<NodeDto> getNodesByType(
            WorkflowResponse workflow,
            String type) {

        return workflow.getNodes()
                .stream()
                .filter(n -> type.equalsIgnoreCase(n.getType()))
                .collect(Collectors.toList());
    }

    /**
     * Find first node of a type.
     */
    public static Optional<NodeDto> findFirstNodeByType(
            WorkflowResponse workflow,
            String type) {

        return workflow.getNodes()
                .stream()
                .filter(n -> type.equalsIgnoreCase(n.getType()))
                .findFirst();
    }

    /**
     * Whether node has multiple incoming flows.
     */
    public static boolean hasMultipleIncoming(
            WorkflowResponse workflow,
            String nodeId) {

        return incomingCount(workflow, nodeId) > 1;
    }

    /**
     * Whether node has multiple outgoing flows.
     */
    public static boolean hasMultipleOutgoing(
            WorkflowResponse workflow,
            String nodeId) {

        return outgoingCount(workflow, nodeId) > 1;
    }

    /**
     * Generate gateway node.
     */
    public static NodeDto createExclusiveGateway(
            String gatewayDirection) {

        NodeDto node = new NodeDto();

        node.setId(generateGatewayId());
        node.setName("Auto Generated Gateway");
        node.setType("EXCLUSIVE_GATEWAY");
        node.setGatewayDirection(gatewayDirection);

        return node;
    }

    /**
     * Deep copy connection.
     */
    public static ConnectionDto copy(ConnectionDto original) {

        ConnectionDto copy = new ConnectionDto();

//        copy.setId(original.getId());
        copy.setSource(original.getSource());
        copy.setTarget(original.getTarget());
        copy.setName(original.getName());
        copy.setConditionExpression(original.getConditionExpression());
        copy.setDefaultFlow(original.isDefaultFlow());

        return copy;
    }

    /**
     * Build node lookup map.
     */
    public static Map<String, NodeDto> nodeMap(
            WorkflowResponse workflow) {

        return workflow.getNodes()
                .stream()
                .collect(Collectors.toMap(
                        NodeDto::getId,
                        n -> n
                ));
    }
    public static List<SequenceFlow> getIncomingFlows(
            Process process,
            String nodeId) {

        List<SequenceFlow> incoming = new ArrayList<>();

        for (FlowElement element : process.getFlowElements()) {

            if (element instanceof SequenceFlow flow
                    && nodeId.equals(flow.getTargetRef())) {

                incoming.add(flow);
            }
        }

        return incoming;
    }

    public static List<SequenceFlow> getOutgoingFlows(
            Process process,
            String nodeId) {

        List<SequenceFlow> outgoing = new ArrayList<>();

        for (FlowElement element : process.getFlowElements()) {

            if (element instanceof SequenceFlow flow
                    && nodeId.equals(flow.getSourceRef())) {

                outgoing.add(flow);
            }
        }

        return outgoing;
    }

    public static FlowElement findNode(
            Process process,
            String nodeId) {

        for (FlowElement element : process.getFlowElements()) {

            if (element instanceof FlowElement node
                    && nodeId.equals(node.getId())) {

                return node;
            }
        }

        return null;
    }

    public static SequenceFlow findFlow(
            Process process,
            String flowId) {

        for (FlowElement element : process.getFlowElements()) {

            if (element instanceof SequenceFlow flow
                    && flowId.equals(flow.getId())) {

                return flow;
            }
        }

        return null;
    }
}
