package com.bamoe.bpmnGererator.serializer.writer;



import com.bamoe.bpmnGererator.model.bpmn.*;
import com.bamoe.bpmnGererator.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EventWriter {

    public void write(Document document,
                      Element processElement,
                      FlowNode node) {

        String tag;

        if (node instanceof StartEvent) {

            tag = "startEvent";

        } else if (node instanceof EndEvent) {

            tag = "endEvent";

        } else {

            return;

        }

        Element element =
                XmlUtil.create(document, processElement, tag);

        XmlUtil.attr(element, "id", node.getId());

        for (String incoming : node.getIncoming()) {

            XmlUtil.text(document,
                    element,
                    "incoming",
                    incoming);

        }

        for (String outgoing : node.getOutgoing()) {

            XmlUtil.text(document,
                    element,
                    "outgoing",
                    outgoing);

        }

    }

}