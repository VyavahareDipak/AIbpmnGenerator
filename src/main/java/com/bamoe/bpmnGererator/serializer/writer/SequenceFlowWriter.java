package com.bamoe.bpmnGererator.serializer.writer;



import com.bamoe.bpmnGererator.model.bpmn.SequenceFlow;
import com.bamoe.bpmnGererator.util.XmlUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class SequenceFlowWriter implements BpmnElementWriter<SequenceFlow> {

    @Override
    public Class<SequenceFlow> supports() {
        return SequenceFlow.class;
    }

    @Override
    public void write(Document document,
                      Element processElement,
                      SequenceFlow flow) {

        Element element =
                XmlUtil.create(document, processElement, "sequenceFlow");

        XmlUtil.attr(element, "id", flow.getId());
        XmlUtil.attr(element, "sourceRef", flow.getSourceRef());
        XmlUtil.attr(element, "targetRef", flow.getTargetRef());

        if (flow.getName() != null) {
            XmlUtil.attr(element, "name", flow.getName());
        }

        if (flow.getConditionExpression() != null &&
                !flow.getConditionExpression().isBlank()) {

            Element condition =
                    XmlUtil.create(document,
                            element,
                            "conditionExpression");

            condition.setAttribute(
                    "xsi:type",
                    "tFormalExpression");

            condition.setTextContent(flow.getConditionExpression());
        }

    }
}