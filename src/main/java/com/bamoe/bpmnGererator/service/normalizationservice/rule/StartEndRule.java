package com.bamoe.bpmnGererator.service.normalizationservice.rule;

import com.bamoe.bpmnGererator.dto.NodeDto;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.util.WorkflowNormalizationUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartEndRule implements WorkflowNormalizationRule {
    @Override
    public void normalize(WorkflowResponse workflow) {

        ensureSingleStartEvent(workflow);
        ensureAtLeastOneEndEvent(workflow);

    }

    private void ensureSingleStartEvent(WorkflowResponse workflow) {

        List<NodeDto> starts =
                WorkflowNormalizationUtil.getNodesByType(workflow, "START_EVENT");

        if (starts.isEmpty()) {

            NodeDto start = new NodeDto();
            start.setId("start");
            start.setName("Start");
            start.setType("START_EVENT");

            WorkflowNormalizationUtil.addNode(workflow, start);

        } else if (starts.size() > 1) {

            throw new IllegalStateException(
                    "Workflow contains multiple START_EVENT nodes.");

        }
    }

    private void ensureAtLeastOneEndEvent(WorkflowResponse workflow) {

        List<NodeDto> ends =
                WorkflowNormalizationUtil.getNodesByType(workflow, "END_EVENT");

        if (ends.isEmpty()) {

            NodeDto end = new NodeDto();
            end.setId("end");
            end.setName("End");
            end.setType("END_EVENT");

            WorkflowNormalizationUtil.addNode(workflow, end);

        }
    }
}
