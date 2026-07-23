package com.bamoe.bpmnGererator.serializer.writer;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DefinitionsWriter {

    public Element write(Document document) {

        Element definitions = document.createElement("definitions");

        definitions.setAttribute(
                "xmlns",
                "http://www.omg.org/spec/BPMN/20100524/MODEL");

        definitions.setAttribute(
                "xmlns:bpmndi",
                "http://www.omg.org/spec/BPMN/20100524/DI");

        definitions.setAttribute(
                "xmlns:dc",
                "http://www.omg.org/spec/DD/20100524/DC");

        definitions.setAttribute(
                "xmlns:di",
                "http://www.omg.org/spec/DD/20100524/DI");

        definitions.setAttribute(
                "xmlns:drools",
                "http://www.jboss.org/drools");

        definitions.setAttribute(
                "xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");

        definitions.setAttribute(
                "targetNamespace",
                "https://kie.apache.org/bpmn");

        document.appendChild(definitions);

        return definitions;
    }

}