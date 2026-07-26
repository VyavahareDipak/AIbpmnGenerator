package com.bamoe.bpmnGererator.serializer.writer;

import com.bamoe.bpmnGererator.model.bpmn.FlowElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface BpmnElementWriter<T extends FlowElement> {

    Class<T> supports();

    void write(Document document,
               Element processElement,
               T element);

}
