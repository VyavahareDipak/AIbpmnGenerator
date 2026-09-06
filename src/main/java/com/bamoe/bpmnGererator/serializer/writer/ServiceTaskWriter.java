package com.bamoe.bpmnGererator.serializer.writer;


import com.bamoe.bpmnGererator.model.bpmn.ServiceTask;
import com.bamoe.bpmnGererator.util.XmlUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
@Component
public class ServiceTaskWriter implements BpmnElementWriter<ServiceTask> {

    @Override
    public Class<ServiceTask> supports() {
        return ServiceTask.class;
    }

    @Override
    public void write(Document document,
                      Element processElement,
                      ServiceTask task) {

        Element element =
                XmlUtil.create(document, processElement, "serviceTask");

        XmlUtil.attr(element, "id", task.getId());
        XmlUtil.attr(element, "name", task.getName());

        task.getIncoming()
                .forEach(i -> XmlUtil.text(document, element, "incoming", i));

        task.getOutgoing()
                .forEach(o -> XmlUtil.text(document, element, "outgoing", o));
    }
}