package com.bamoe.bpmnGererator.serializer.writer;

import com.bamoe.bpmnGererator.dto.NodeVariableDto;
import com.bamoe.bpmnGererator.model.bpmn.UserTask;
import com.bamoe.bpmnGererator.serializer.XmlUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
@Component

public class UserTaskWriter implements BpmnElementWriter<UserTask> {

    @Override
    public Class<UserTask> supports() {
        return UserTask.class;
    }

    @Override
    public void write(Document document,
                      Element processElement,
                      UserTask task) {

        Element userTask = document.createElement("userTask");

        userTask.setAttribute("id", task.getId());

        if (task.getName() != null) {
            userTask.setAttribute("name", task.getName());
        }

        processElement.appendChild(userTask);

        writeExtensionElements(document, userTask, task);

        for (String incoming : task.getIncoming()) {

            Element in = document.createElement("incoming");
            in.setTextContent(incoming);
            userTask.appendChild(in);

        }

        for (String outgoing : task.getOutgoing()) {

            Element out = document.createElement("outgoing");
            out.setTextContent(outgoing);
            userTask.appendChild(out);

        }

        writeIoSpecification(document, userTask, task);

        writeInputAssociations(document, userTask, task);

        writeOutputAssociations(document, userTask, task);

    }

    private void writeExtensionElements(Document document,
                                        Element taskElement,
                                        UserTask task) {

        Element extension =
                document.createElement("extensionElements");

        taskElement.appendChild(extension);

        addMetaData(document, extension,
                "customAsync", "false");

        addMetaData(document, extension,
                "customAutoStart", "false");

        addMetaData(document, extension,
                "elementname", task.getName());

    }

    private void addMetaData(Document document,
                             Element extension,
                             String name,
                             String value) {

        Element meta =
                document.createElement("drools:metaData");

        meta.setAttribute("name", name);

        Element metaValue =
                document.createElement("drools:metaValue");

        metaValue.setTextContent(value);

        meta.appendChild(metaValue);

        extension.appendChild(meta);

    }

    private void writeIoSpecification(Document document,
                                      Element taskElement,
                                      UserTask task) {

        if ((task.getInputs() == null || task.getInputs().isEmpty()) &&
                (task.getOutputs() == null || task.getOutputs().isEmpty())) {

            return;
        }

        Element io =
                document.createElement("ioSpecification");

        io.setAttribute(
                "id",
                "IoSpecification_" + task.getId());

        taskElement.appendChild(io);

        Element inputSet =
                document.createElement("inputSet");

        inputSet.setAttribute(
                "id",
                "InputSet_" + task.getId());

        io.appendChild(inputSet);

        if (task.getInputs() != null) {

            for (NodeVariableDto variable : task.getInputs()) {

                String inputId =
                        "DataInput_" + task.getId() + "_" + variable.getVariable();

                Element input =
                        document.createElement("dataInput");

                input.setAttribute("id", inputId);

                input.setAttribute(
                        "name",
                        variable.getVariable());

                input.setAttribute(
                        "drools:dtype",
                        variable.getType());

                input.setAttribute(
                        "itemSubjectRef",
                        "ItemDefinition_" + variable.getVariable());

                io.appendChild(input);

                Element ref =
                        document.createElement("dataInputRefs");

                ref.setTextContent(inputId);

                inputSet.appendChild(ref);

            }

        }

        Element outputSet =
                document.createElement("outputSet");

        outputSet.setAttribute(
                "id",
                "OutputSet_" + task.getId());

        io.appendChild(outputSet);

        if (task.getOutputs() != null) {

            for (NodeVariableDto variable : task.getOutputs()) {

                String outputId =
                        "DataOutput_" + task.getId() + "_" + variable.getVariable();

                Element output =
                        document.createElement("dataOutput");

                output.setAttribute("id", outputId);

                output.setAttribute(
                        "name",
                        variable.getVariable());

                output.setAttribute(
                        "drools:dtype",
                        variable.getType());

                output.setAttribute(
                        "itemSubjectRef",
                        "ItemDefinition_" + variable.getVariable());

                io.appendChild(output);

                Element ref =
                        document.createElement("dataOutputRefs");

                ref.setTextContent(outputId);

                outputSet.appendChild(ref);

            }

        }

    }

    private void writeInputAssociations(Document document,
                                        Element taskElement,
                                        UserTask task) {

        if (task.getInputs() == null) {
            return;
        }

        for (NodeVariableDto variable : task.getInputs()) {

            Element association =
                    document.createElement("dataInputAssociation");

            association.setAttribute(
                    "id",
                    "InputAssociation_" +
                            task.getId() +
                            "_" +
                            variable.getVariable());

            Element source =
                    document.createElement("sourceRef");

            source.setTextContent(
                    "Property_" + variable.getVariable());

            association.appendChild(source);

            Element target =
                    document.createElement("targetRef");

            target.setTextContent(
                    "DataInput_" +
                            task.getId() +
                            "_" +
                            variable.getVariable());

            association.appendChild(target);

            taskElement.appendChild(association);

        }

    }

    private void writeOutputAssociations(Document document,
                                         Element taskElement,
                                         UserTask task) {

        if (task.getOutputs() == null) {
            return;
        }

        for (NodeVariableDto variable : task.getOutputs()) {

            Element association =
                    document.createElement("dataOutputAssociation");

            association.setAttribute(
                    "id",
                    "OutputAssociation_" +
                            task.getId() +
                            "_" +
                            variable.getVariable());

            Element source =
                    document.createElement("sourceRef");

            source.setTextContent(
                    "DataOutput_" +
                            task.getId() +
                            "_" +
                            variable.getVariable());

            association.appendChild(source);

            Element target =
                    document.createElement("targetRef");

            target.setTextContent(
                    "Property_" + variable.getVariable());

            association.appendChild(target);

            taskElement.appendChild(association);

        }

    }
}