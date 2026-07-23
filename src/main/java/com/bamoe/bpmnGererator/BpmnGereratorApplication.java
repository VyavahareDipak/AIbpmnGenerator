package com.bamoe.bpmnGererator;

import com.bamoe.bpmnGererator.converter.WorkflowToBpmnConverter;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.model.bpmn.Definitions;
import com.bamoe.bpmnGererator.serializer.BpmnSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class BpmnGereratorApplication {
	static String json = """
			{
			   "processId": "makerChecker",
			   "processName": "Maker Checker Workflow",
			   "nodes": [
			     {
			       "id": "start",
			       "type": "START_EVENT",
			       "name": "Start"
			     },
			     {
			       "id": "maker",
			       "type": "USER_TASK",
			       "name": "Maker"
			     },
			     {
			       "id": "checker",
			       "type": "USER_TASK",
			       "name": "Checker"
			     },
			     {
			       "id": "end",
			       "type": "END_EVENT",
			       "name": "End"
			     }
			   ],
			   "connections": [
			     {
			       "source": "start",
			       "target": "maker"
			     },
			     {
			       "source": "maker",
			       "target": "checker"
			     },
			     {
			       "source": "checker",
			       "target": "end"
			     }
			   ]
			 }
						""";

	public static void main(String[] args) {

		SpringApplication.run(BpmnGereratorApplication.class, args);


		ObjectMapper mapper = new ObjectMapper();

		WorkflowResponse response =
				mapper.readValue(json, WorkflowResponse.class);

		WorkflowToBpmnConverter converter =
				new WorkflowToBpmnConverter();

		System.out.println("1. JSON Parsed");


		Definitions definitions = converter.convert(response);

		System.out.println("2. Converted");

		BpmnSerializer serializer = new BpmnSerializer();

		System.out.println("3. Before Serialize");
		try {
			Path path = Paths.get("workflow.bpmn");

			System.out.println(path.toAbsolutePath());
			serializer.serialize(definitions,path);
		}catch (Exception e){
			e.printStackTrace();
		}


		System.out.println("4. After Serialize");

	}

}
