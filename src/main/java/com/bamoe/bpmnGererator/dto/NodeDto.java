package com.bamoe.bpmnGererator.dto;

import lombok.Data;

import java.util.List;

@Data
public class NodeDto {
    private String  id ;
    private String  type ;
    private String name ;
    private List<NodeVariableDto> inputs;
    private List<NodeVariableDto> outputs;
    private String gatewayDirection ;

}
