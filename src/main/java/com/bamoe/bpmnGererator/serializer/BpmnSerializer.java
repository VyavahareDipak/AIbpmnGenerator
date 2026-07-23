package com.bamoe.bpmnGererator.serializer;


import com.bamoe.bpmnGererator.model.bpmn.*;
import com.bamoe.bpmnGererator.serializer.writer.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.nio.file.Path;

public class BpmnSerializer {

    private final DefinitionsWriter definitionsWriter =
            new DefinitionsWriter();

    private final ProcessWriter processWriter =
            new ProcessWriter();

    private final EventWriter eventWriter =
            new EventWriter();

    private final TaskWriter taskWriter =
            new TaskWriter();

    private final GatewayWriter gatewayWriter =
            new GatewayWriter();

    private final SequenceFlowWriter sequenceFlowWriter =
            new SequenceFlowWriter();

    public void serialize(
            Definitions definitions,
            Path output) throws Exception {

        Document document =
                DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .newDocument();

        Element definitionsElement =
                definitionsWriter.write(document);

        Element processElement =
                processWriter.write(
                        document,
                        definitionsElement,
                        definitions.getProcess());

        for (FlowElement element :
                definitions.getProcess().getFlowElements()) {

            if (element instanceof FlowNode node) {

                eventWriter.write(
                        document,
                        processElement,
                        node);

                taskWriter.write(
                        document,
                        processElement,
                        node);

                gatewayWriter.write(
                        document,
                        processElement,
                        node);

            }

            if (element instanceof SequenceFlow flow) {

                sequenceFlowWriter.write(
                        document,
                        processElement,
                        flow);

            }

        }

        Transformer transformer =
                TransformerFactory
                        .newInstance()
                        .newTransformer();

        transformer.setOutputProperty(
                OutputKeys.INDENT,
                "yes");

        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount",
                "2");

        transformer.transform(
                new DOMSource(document),
                new StreamResult(output.toFile()));

    }

}