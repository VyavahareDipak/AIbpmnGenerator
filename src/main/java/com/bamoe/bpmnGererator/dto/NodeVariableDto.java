package com.bamoe.bpmnGererator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class NodeVariableDto {

    private String variable;

    private String type;

}