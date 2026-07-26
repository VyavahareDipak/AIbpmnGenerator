package com.bamoe.bpmnGererator.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkflowResponse {

   private String processId ;
    private String processName ;
    List<NodeDto> nodes ;
    List<ConnectionDto> connections ;
    private List<VariableDto> variables;
}
