package com.bamoe.bpmnGererator.service;

import com.bamoe.bpmnGererator.service.aiservice.AiWorkflowGenerationService;
import com.bamoe.bpmnGererator.converter.BpmnToWorkflowConverter;
import com.bamoe.bpmnGererator.converter.WorkflowToBpmnConverter;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.dto.request.BpmnGenerationRequest;
import com.bamoe.bpmnGererator.dto.request.BpmnUpdateRequestDto;
import com.bamoe.bpmnGererator.dto.request.WorkflowUpdateLLMRequestDto;
import com.bamoe.bpmnGererator.model.bpmn.Definitions;
import com.bamoe.bpmnGererator.service.normalizationservice.WorkflowResponseNormalizationService;
import com.bamoe.bpmnGererator.serializer.BpmnSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class BpmnService {

    @Autowired
    private AiWorkflowGenerationService workflowGenerationService ;

    @Autowired
    WorkflowResponseNormalizationService workflowResponseNormalizationService;

    @Autowired
    BpmnToWorkflowConverter bpmnToWorkflowConverter ;

    public ResponseEntity<ByteArrayResource> generateBpmn(BpmnGenerationRequest request) throws Exception {

        WorkflowResponse workflow = workflowGenerationService.generate(request.getPrompt());
        workflowResponseNormalizationService.normalize(workflow);
        Definitions definitions = new WorkflowToBpmnConverter().convert(workflow);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        new BpmnSerializer().serialize(
                definitions,
                output,
                workflow
        );

        ByteArrayResource resource = new ByteArrayResource(output.toByteArray());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(definitions.getProcess().getId() + ".bpmn")
                                .build()
                                .toString()
                )
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_XML)
                .body(resource);

    }

    public ResponseEntity<ByteArrayResource> updateBpmn(BpmnUpdateRequestDto bpmnUpdateRequestDto) throws Exception {
        WorkflowResponse workflow = bpmnToWorkflowConverter.convert(bpmnUpdateRequestDto.getBpmnXmlString());
        ObjectMapper mapper = new ObjectMapper();

        WorkflowUpdateLLMRequestDto request = new WorkflowUpdateLLMRequestDto(bpmnUpdateRequestDto.getPrompt(),workflow);

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

        workflow = workflowGenerationService.updateBpmn(json);

        workflowResponseNormalizationService.normalize(workflow);
        Definitions definitions = new WorkflowToBpmnConverter().convert(workflow);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new BpmnSerializer().serialize(definitions, output, workflow);
        ByteArrayResource resource = new ByteArrayResource(output.toByteArray());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(definitions.getProcess().getId() + ".bpmn")
                                .build()
                                .toString()
                )
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_XML)
                .body(resource);
    } ;
}

