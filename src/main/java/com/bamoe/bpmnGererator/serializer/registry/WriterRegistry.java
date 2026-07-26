package com.bamoe.bpmnGererator.serializer.registry;

import com.bamoe.bpmnGererator.model.bpmn.FlowElement;
import com.bamoe.bpmnGererator.serializer.writer.BpmnElementWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriterRegistry {

    private final Map<Class<?>, BpmnElementWriter<?>> writers =
            new HashMap<>();

    public WriterRegistry(List<BpmnElementWriter<?>> writerList) {

        for (BpmnElementWriter<?> writer : writerList) {
            writers.put(writer.supports(), writer);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends FlowElement> BpmnElementWriter<T> getWriter(Class<?> clazz) {
        return (BpmnElementWriter<T>) writers.get(clazz);
    }

}
