package com.bamoe.bpmnGererator.controller;

import com.bamoe.bpmnGererator.dto.request.BpmnGenerationRequest;
import com.bamoe.bpmnGererator.dto.request.BpmnUpdateRequestDto;
import com.bamoe.bpmnGererator.service.BpmnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/bpmn")
public class BpmnController {

    @Autowired
    private BpmnService bpmnService ;

    @PostMapping(
            value = "/generate",
            produces = "application/xml"
    )
    public ResponseEntity<ByteArrayResource> generateBpmn(@Valid @RequestBody BpmnGenerationRequest request) throws Exception {
       return  bpmnService.generateBpmn(request);
    }

    @PostMapping("/update")
    public ResponseEntity<ByteArrayResource> updateBpmn(@Valid @RequestBody BpmnUpdateRequestDto bpmnUpdateRequestDto) throws Exception {
       return bpmnService.updateBpmn(bpmnUpdateRequestDto) ;
    } ;

}
