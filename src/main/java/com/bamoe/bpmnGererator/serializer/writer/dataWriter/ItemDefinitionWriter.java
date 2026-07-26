package com.bamoe.bpmnGererator.serializer.writer.dataWriter;

import com.bamoe.bpmnGererator.dto.VariableDto;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

public class ItemDefinitionWriter {

    public void write(
            Document document,
            Element definitionsElement,
            List<VariableDto> variables) {

        if (variables == null || variables.isEmpty()) {
            return;
        }

        for (VariableDto variable : variables) {

            Element itemDefinition =
                    document.createElement("itemDefinition");

            itemDefinition.setAttribute(
                    "id",
                    "ItemDefinition_" + variable.getName());

            itemDefinition.setAttribute(
                    "structureRef",
                    variable.getType());

            definitionsElement.appendChild(itemDefinition);
        }
    }

}