package com.bamoe.bpmnGererator.serializer.writer;



import com.bamoe.bpmnGererator.model.bpmn.*;
import com.bamoe.bpmnGererator.serializer.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SequenceFlowWriter {

    public void write(Document document,
                      Element processElement,
                      SequenceFlow flow) {

        Element element =
                XmlUtil.create(document,
                        processElement,
                        "sequenceFlow");

        XmlUtil.attr(element,
                "id",
                flow.getId());

        XmlUtil.attr(element,
                "sourceRef",
                flow.getSourceRef());

        XmlUtil.attr(element,
                "targetRef",
                flow.getTargetRef());

    }

}