package com.bamoe.bpmnGererator.serializer.writer;

import com.bamoe.bpmnGererator.model.bpmn.StartEvent;
import com.bamoe.bpmnGererator.serializer.XmlUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
@Component
public class StartEventWriter implements BpmnElementWriter<StartEvent> {

    @Override
    public Class<StartEvent> supports() {
        return StartEvent.class;
    }

    @Override
    public void write(Document document,
                      Element processElement,
                      StartEvent event) {

        Element element =
                XmlUtil.create(document, processElement, "startEvent");

        XmlUtil.attr(element, "id", event.getId());

        event.getOutgoing()
                .forEach(o -> XmlUtil.text(document, element, "outgoing", o));
    }
}