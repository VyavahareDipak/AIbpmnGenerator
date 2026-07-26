package com.bamoe.bpmnGererator.serializer.writer;


import com.bamoe.bpmnGererator.model.bpmn.*;
import com.bamoe.bpmnGererator.model.bpmn.Process;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProcessWriter {

    public Element write(
            Document document,
            Element definitions,
            Process process) {

        Element processElement =
                document.createElement("process");

        processElement.setAttribute(
                "id",
                process.getId());

        if (process.getName() != null) {

            processElement.setAttribute(
                    "name",
                    process.getName());

        }


        definitions.appendChild(processElement);

        return processElement;
    }

}