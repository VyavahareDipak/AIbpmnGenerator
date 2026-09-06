package com.bamoe.bpmnGererator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariableDto {
    private String name;
    private String type;
}
