package com.bamoe.bpmnGererator.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BpmnUpdateRequestDto {

    @NotBlank(message = "Prompt cannot be empty")
    private String prompt ;
    @NotBlank(message = "Prompt cannot be empty")
    private String bpmnXmlString ;

}
