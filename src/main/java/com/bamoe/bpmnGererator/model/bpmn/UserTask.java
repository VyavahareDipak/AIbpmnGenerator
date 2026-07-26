package com.bamoe.bpmnGererator.model.bpmn;

import com.bamoe.bpmnGererator.dto.NodeVariableDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserTask extends Activity {
    private List<NodeVariableDto> inputs;
    private List<NodeVariableDto> outputs;
}