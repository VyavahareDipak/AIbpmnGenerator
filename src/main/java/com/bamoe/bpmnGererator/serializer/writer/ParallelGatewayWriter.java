package com.bamoe.bpmnGererator.serializer.writer;


import com.bamoe.bpmnGererator.model.bpmn.ParallelGateway;
import com.bamoe.bpmnGererator.util.XmlUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class ParallelGatewayWriter implements BpmnElementWriter<ParallelGateway> {

    @Override
    public Class<ParallelGateway> supports() {
        return ParallelGateway.class;
    }

    @Override
    public void write(Document document,
                      Element processElement,
                      ParallelGateway gateway) {

        Element element =
                XmlUtil.create(document, processElement, "parallelGateway");

        XmlUtil.attr(element, "id", gateway.getId());
        XmlUtil.attr(element, "name", gateway.getName());

        gateway.getIncoming()
                .forEach(i -> XmlUtil.text(document, element, "incoming", i));

        gateway.getOutgoing()
                .forEach(o -> XmlUtil.text(document, element, "outgoing", o));

        XmlUtil.attr(element,"gatewayDirection" , gateway.getGatewayDirection());
    }
}