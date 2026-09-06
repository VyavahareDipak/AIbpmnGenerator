package com.bamoe.bpmnGererator.serializer.writer;

import com.bamoe.bpmnGererator.model.bpmn.ScriptTask;
import com.bamoe.bpmnGererator.util.XmlUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class ScriptTaskWriter implements BpmnElementWriter<ScriptTask> {

    @Override
    public Class<ScriptTask> supports() {
        return ScriptTask.class;
    }

    @Override
    public void write(Document document,
                      Element processElement,
                      ScriptTask task) {

        Element element =
                XmlUtil.create(document, processElement, "scriptTask");

        XmlUtil.attr(element, "id", task.getId());
        XmlUtil.attr(element, "name", task.getName());

        task.getIncoming()
                .forEach(i -> XmlUtil.text(document, element, "incoming", i));

        task.getOutgoing()
                .forEach(o -> XmlUtil.text(document, element, "outgoing", o));

        // Future:
        // XmlUtil.attr(element, "scriptFormat", task.getScriptFormat());
        // XmlUtil.text(document, element, "script", task.getScript());
    }
}
