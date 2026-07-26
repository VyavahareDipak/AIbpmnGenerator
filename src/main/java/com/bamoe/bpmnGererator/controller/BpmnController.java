package com.bamoe.bpmnGererator.controller;

import com.bamoe.bpmnGererator.ai.service.WorkflowGenerationService;
import com.bamoe.bpmnGererator.converter.WorkflowToBpmnConverter;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.model.bpmn.Definitions;
import com.bamoe.bpmnGererator.serializer.BpmnSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class BpmnController {

    @Autowired
    private WorkflowGenerationService workflowGenerationService ;

    @GetMapping
    public int generateBpmn(@RequestBody String promt){
        WorkflowResponse response =
                workflowGenerationService.generate(promt);

        WorkflowToBpmnConverter converter =
                new WorkflowToBpmnConverter();

        System.out.println("1. JSON Parsed");


        Definitions definitions = converter.convert(response);

        System.out.println("2. Converted");

        BpmnSerializer serializer = new BpmnSerializer();

        System.out.println("3. Before Serialize");
        try {
            Path path = Paths.get("parallel.bpmn");

            System.out.println(path.toAbsolutePath());
            serializer.serialize(definitions,path, response);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("4. After Serialize");
        return  200 ;
    }


}
