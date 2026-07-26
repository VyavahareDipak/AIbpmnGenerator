package com.bamoe.bpmnGererator.serializer.writer.dataWriter;

import com.bamoe.bpmnGererator.dto.VariableDto;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

public class PropertyWriter {

    public void write(
            Document document,
            Element processElement,
            List<VariableDto> variables) {

        if (variables == null || variables.isEmpty()) {
            return;
        }

        for (VariableDto variable : variables) {

            Element property = document.createElement("property");

            property.setAttribute(
                    "id",
                    "Property_" + variable.getName());

            property.setAttribute(
                    "name",
                    variable.getName());

            property.setAttribute(
                    "itemSubjectRef",
                    "ItemDefinition_" + variable.getName());

            // <extensionElements>
            Element extensionElements =
                    document.createElement("extensionElements");

            property.appendChild(extensionElements);

            // <drools:metaData>
            Element metaData =
                    document.createElement("drools:metaData");

            metaData.setAttribute(
                    "name",
                    "customTags");

            extensionElements.appendChild(metaData);

            // <drools:metaValue>
            Element metaValue =
                    document.createElement("drools:metaValue");

            metaValue.setTextContent("internal");

            metaData.appendChild(metaValue);

            processElement.appendChild(property);
        }
    }
}