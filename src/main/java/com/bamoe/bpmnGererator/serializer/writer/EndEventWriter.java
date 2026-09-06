package com.bamoe.bpmnGererator.serializer.writer;

import com.bamoe.bpmnGererator.model.bpmn.EndEvent;
import com.bamoe.bpmnGererator.util.XmlUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class EndEventWriter implements BpmnElementWriter<EndEvent> {

    @Override
    public Class<EndEvent> supports() {
        return EndEvent.class;
    }

    @Override
    public void write(Document document,
                      Element processElement,
                      EndEvent event) {

        Element element =
                XmlUtil.create(document, processElement, "endEvent");

        XmlUtil.attr(element, "id", event.getId());

        event.getIncoming()
                .forEach(i -> XmlUtil.text(document, element, "incoming", i));
    }
}