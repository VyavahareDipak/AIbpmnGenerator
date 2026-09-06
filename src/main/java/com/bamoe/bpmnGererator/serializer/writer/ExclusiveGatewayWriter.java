package com.bamoe.bpmnGererator.serializer.writer;

import com.bamoe.bpmnGererator.model.bpmn.ExclusiveGateway;
import com.bamoe.bpmnGererator.util.XmlUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class ExclusiveGatewayWriter implements BpmnElementWriter<ExclusiveGateway> {

    @Override
    public Class<ExclusiveGateway> supports() {
        return ExclusiveGateway.class;
    }

    @Override
    public void write(Document document,
                      Element processElement,
                      ExclusiveGateway gateway) {

        Element element =
                XmlUtil.create(document, processElement, "exclusiveGateway");

        XmlUtil.attr(element, "id", gateway.getId());
        XmlUtil.attr(element, "name", gateway.getName());

        gateway.getIncoming()
                .forEach(i -> XmlUtil.text(document, element, "incoming", i));

        gateway.getOutgoing()
                .forEach(o -> XmlUtil.text(document, element, "outgoing", o));

        // Future:
        String defaultFlowId = gateway.getDefaultFlowId();

        if (defaultFlowId != null) {
            XmlUtil.attr(element, "default", defaultFlowId);
        }
         XmlUtil.attr(element, "gatewayDirection",gateway.getGatewayDirection());
    }
}