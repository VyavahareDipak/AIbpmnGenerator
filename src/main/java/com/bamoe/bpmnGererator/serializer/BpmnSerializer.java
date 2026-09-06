package com.bamoe.bpmnGererator.serializer;


import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.model.bpmn.FlowElement;
import com.bamoe.bpmnGererator.model.bpmn.Definitions;
import com.bamoe.bpmnGererator.serializer.diagram.DiagramWriter;
import com.bamoe.bpmnGererator.serializer.registry.WriterRegistry;
import com.bamoe.bpmnGererator.serializer.writer.*;

import com.bamoe.bpmnGererator.serializer.writer.dataWriter.ItemDefinitionWriter;
import com.bamoe.bpmnGererator.serializer.writer.dataWriter.PropertyWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

public class BpmnSerializer {

    private final WriterRegistry registry =
            new WriterRegistry(List.of(
                    new StartEventWriter(),
                    new EndEventWriter(),
                    new UserTaskWriter(),
                    new ServiceTaskWriter(),
                    new ScriptTaskWriter(),
                    new ExclusiveGatewayWriter(),
                    new ParallelGatewayWriter(),
                    new SequenceFlowWriter()
            ));
    private final DefinitionsWriter definitionsWriter = new DefinitionsWriter();
    private final ProcessWriter processWriter = new ProcessWriter() ;
    private final ItemDefinitionWriter itemDefinitionWriter = new ItemDefinitionWriter();
    private final PropertyWriter propertyWriter = new PropertyWriter();

    public void serialize(
            Definitions definitions,
            OutputStream outputStream,
            WorkflowResponse workflowResponse) throws Exception {

        // Existing serialization logic
        Document document =
                DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .newDocument();

        Element definitionsElement =
                definitionsWriter.write(document);

        itemDefinitionWriter.write(
                document,
                definitionsElement,
                workflowResponse.getVariables());

        Element process =
                processWriter.write(
                        document,
                        definitionsElement,
                        definitions.getProcess());

        propertyWriter.write(
                document,
                process,
                workflowResponse.getVariables());

        for (FlowElement element :
                definitions.getProcess().getFlowElements()) {

            BpmnElementWriter writer =
                    registry.getWriter(element.getClass());

            if (writer == null) {
                throw new IllegalArgumentException(
                        "No writer registered for " + element.getClass().getSimpleName());
            }

            writer.write(document, process, element);
        }
        new DiagramWriter().write(
                document,
                definitionsElement,
                definitions.getProcess());


        Transformer transformer =
                TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        transformer.transform(
                new DOMSource(document),
                new StreamResult(outputStream)
        );
    }
}