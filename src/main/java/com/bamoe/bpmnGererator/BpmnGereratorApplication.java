package com.bamoe.bpmnGererator;

import com.bamoe.bpmnGererator.ai.service.WorkflowGenerationService;
import com.bamoe.bpmnGererator.converter.WorkflowToBpmnConverter;
import com.bamoe.bpmnGererator.dto.WorkflowResponse;
import com.bamoe.bpmnGererator.model.bpmn.Definitions;
import com.bamoe.bpmnGererator.serializer.BpmnSerializer;
import org.springframework.beans.factory.annotation.Autowired;
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
	static String json2  = """
						{
			     "processId": "serviceWorkflow",
			     "processName": "Service Workflow",
			     "nodes": [
			       {
			         "id": "start",
			         "type": "START_EVENT",
			         "name": "Start"
			       },
			       {
			         "id": "validate",
			         "type": "SERVICE_TASK",
			         "name": "Validate Customer"
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
			         "target": "validate"
			       },
			       {
			         "source": "validate",
			         "target": "end"
			       }
			     ]
			   }
			""" ;

	static String json3 = """
			{
			  "processId": "scriptWorkflow",
			  "processName": "Script Workflow",
			  "nodes": [
			    {
			      "id": "start",
			      "type": "START_EVENT"
			    },
			    {
			      "id": "calculate",
			      "type": "SCRIPT_TASK",
			      "name": "Calculate Interest"
			    },
			    {
			      "id": "end",
			      "type": "END_EVENT"
			    }
			  ],
			  "connections": [
			    {
			      "source": "start",
			      "target": "calculate"
			    },
			    {
			      "source": "calculate",
			      "target": "end"
			    }
			  ]
			}
			""" ;
static String json4 = """
		{
		  "processId": "loanApproval",
		  "processName": "Loan Approval",
		  "nodes": [
		    {
		      "id": "start",
		      "type": "START_EVENT"
		    },
		    {
		      "id": "maker",
		      "type": "USER_TASK",
		      "name": "Maker"
		    },
		    {
		      "id": "validation",
		      "type": "SERVICE_TASK",
		      "name": "Validate Request"
		    },
		    {
		      "id": "checker",
		      "type": "USER_TASK",
		      "name": "Checker"
		    },
		    {
		      "id": "end",
		      "type": "END_EVENT"
		    }
		  ],
		  "connections": [
		    {
		      "source": "start",
		      "target": "maker"
		    },
		    {
		      "source": "maker",
		      "target": "validation"
		    },
		    {
		      "source": "validation",
		      "target": "checker"
		    },
		    {
		      "source": "checker",
		      "target": "end"
		    }
		  ]
		}
		""" ;

static String json5 = """
		{
		  "processId": "exclusiveFlow",
		  "processName": "Approval Decision",
		  "nodes": [
		    {
		      "id": "start",
		      "type": "START_EVENT"
		    },
		    {
		      "id": "maker",
		      "type": "USER_TASK",
		      "name": "Maker"
		    },
		    {
		      "id": "decision",
		      "type": "EXCLUSIVE_GATEWAY",
		      "name": "Approved?"
		    },
		    {
		      "id": "approve",
		      "type": "USER_TASK",
		      "name": "Approve"
		    },
		    {
		      "id": "reject",
		      "type": "SCRIPT_TASK",
		      "name": "Reject"
		    },
		    {
		      "id": "end",
		      "type": "END_EVENT"
		    }
		  ],
		  "connections": [
		    {
		      "source": "start",
		      "target": "maker"
		    },
		    {
		      "source": "maker",
		      "target": "decision"
		    },
		    {
		      "source": "decision",
		      "target": "approve"
		    },
		    {
		      "source": "decision",
		      "target": "reject"
		    },
		    {
		      "source": "approve",
		      "target": "end"
		    },
		    {
		      "source": "reject",
		      "target": "end"
		    }
		  ]
		}
		""" ;

static String json6 = """
		{
		  "processId": "parallelFlow",
		  "processName": "Parallel Processing",
		  "nodes": [
		    {
		      "id": "start",
		      "type": "START_EVENT"
		    },
		    {
		      "id": "parallelStart",
		      "type": "PARALLEL_GATEWAY",
		      "name": "Fork"
		    },
		    {
		      "id": "taskA",
		      "type": "USER_TASK",
		      "name": "Task A"
		    },
		    {
		      "id": "taskB",
		      "type": "SERVICE_TASK",
		      "name": "Task B"
		    },
		    {
		      "id": "parallelEnd",
		      "type": "PARALLEL_GATEWAY",
		      "name": "Join"
		    },
		    {
		      "id": "end",
		      "type": "END_EVENT"
		    }
		  ],
		  "connections": [
		    {
		      "source": "start",
		      "target": "parallelStart"
		    },
		    {
		      "source": "parallelStart",
		      "target": "taskA"
		    },
		    {
		      "source": "parallelStart",
		      "target": "taskB"
		    },
		    {
		      "source": "taskA",
		      "target": "parallelEnd"
		    },
		    {
		      "source": "taskB",
		      "target": "parallelEnd"
		    },
		    {
		      "source": "parallelEnd",
		      "target": "end"
		    }
		  ]
		}
		""" ;

static  String json7 = """
		{
		  "processId": "employeeOnboarding",
		  "processName": "Employee Onboarding",
		  "nodes": [
		    {
		      "id": "start",
		      "type": "START_EVENT"
		    },
		    {
		      "id": "hrApproval",
		      "type": "USER_TASK",
		      "name": "HR Approval"
		    },
		    {
		      "id": "documents",
		      "type": "SERVICE_TASK",
		      "name": "Generate Documents"
		    },
		    {
		      "id": "parallelStart",
		      "type": "PARALLEL_GATEWAY"
		    },
		    {
		      "id": "itSetup",
		      "type": "SERVICE_TASK",
		      "name": "IT Setup"
		    },
		    {
		      "id": "managerMeeting",
		      "type": "USER_TASK",
		      "name": "Manager Meeting"
		    },
		    {
		      "id": "parallelEnd",
		      "type": "PARALLEL_GATEWAY"
		    },
		    {
		      "id": "finalApproval",
		      "type": "USER_TASK",
		      "name": "Final Approval"
		    },
		    {
		      "id": "end",
		      "type": "END_EVENT"
		    }
		  ],
		  "connections": [
		    {
		      "source": "start",
		      "target": "hrApproval"
		    },
		    {
		      "source": "hrApproval",
		      "target": "documents"
		    },
		    {
		      "source": "documents",
		      "target": "parallelStart"
		    },
		    {
		      "source": "parallelStart",
		      "target": "itSetup"
		    },
		    {
		      "source": "parallelStart",
		      "target": "managerMeeting"
		    },
		    {
		      "source": "itSetup",
		      "target": "parallelEnd"
		    },
		    {
		      "source": "managerMeeting",
		      "target": "parallelEnd"
		    },
		    {
		      "source": "parallelEnd",
		      "target": "finalApproval"
		    },
		    {
		      "source": "finalApproval",
		      "target": "end"
		    }
		  ]
		}
		""" ;
static String  json8 = """
		  
		{
		  "processId": "loanApproval",
		  "processName": "Loan Approval",
		  "nodes": [
		    {
		      "id": "start",
		      "type": "START_EVENT"
		    },
		    {
		      "id": "review",
		      "type": "USER_TASK",
		      "name": "Review Application"
		    },
		    {
		      "id": "decision",
		      "type": "EXCLUSIVE_GATEWAY",
		      "name": "Decision"
		    },
		    {
		      "id": "approve",
		      "type": "USER_TASK",
		      "name": "Approve Loan"
		    },
		    {
		      "id": "reject",
		      "type": "USER_TASK",
		      "name": "Reject Loan"
		    },
		    {
		      "id": "end",
		      "type": "END_EVENT"
		    }
		  ],
		  "connections": [
		    {
		      "source": "start",
		      "target": "review"
		    },
		    {
		      "source": "review",
		      "target": "decision"
		    },
		    {
		      "source": "decision",
		      "target": "approve",
		      "name": "Approved",
		      "conditionExpression": "#{approved == true}"
		    },
		    {
		      "source": "decision",
		      "target": "reject",
		      "name": "Rejected",
		      "conditionExpression": "#{approved == false}",
		      "defaultFlow": true
		    },
		    {
		      "source": "approve",
		      "target": "end"
		    },
		    {
		      "source": "reject",
		      "target": "end"
		    }
		  ]
		}
		""" ;

	static String json10 = """
			{
			  "processId": "loanApproval",
			  "processName": "Loan Approval Workflow",
			  "variables": [
			    {
			      "name": "approved",
			      "type": "Boolean"
			    },
			    {
			      "name": "remarks",
			      "type": "String"
			    }
			  ],
			  "nodes": [
			    {
			      "id": "start",
			      "type": "START_EVENT",
			      "name": "Start"
			    },
			    {
			      "id": "review",
			      "type": "USER_TASK",
			      "name": "Review Application",
			      "inputs": [
			        {
			          "variable": "approved",
			          "type": "Boolean"
			        },
			        {
			          "variable": "remarks",
			          "type": "String"
			        }
			      ],
			      "outputs": [
			        {
			          "variable": "approved",
			          "type": "Boolean"
			        },
			        {
			          "variable": "remarks",
			          "type": "String"
			        }
			      ]
			    },
			    {
			      "id": "decision",
			      "type": "EXCLUSIVE_GATEWAY",
			      "name": "Approval Decision"
			    },
			    {
			      "id": "approveTask",
			      "type": "USER_TASK",
			      "name": "Approve Loan"
			    },
			    {
			      "id": "rejectTask",
			      "type": "USER_TASK",
			      "name": "Reject Loan"
			    },
			    {
			      "id": "approvedEnd",
			      "type": "END_EVENT",
			      "name": "Approved"
			    },
			    {
			      "id": "rejectedEnd",
			      "type": "END_EVENT",
			      "name": "Rejected"
			    }
			  ],
			  "connections": [
			    {
			      "source": "start",
			      "target": "review"
			    },
			    {
			      "source": "review",
			      "target": "decision"
			    },
			    {
			      "source": "decision",
			      "target": "approveTask",
			      "name": "Approved",
			      "conditionExpression": "return approved;"
			    },
			    {
			      "source": "decision",
			      "target": "rejectTask",
			      "name": "Rejected",
			      "defaultFlow": true
			    },
			    {
			      "source": "approveTask",
			      "target": "approvedEnd"
			    },
			    {
			      "source": "rejectTask",
			      "target": "rejectedEnd"
			    }
			  ]
			}
			""" ;

	static String sendBack = """
			{
			  "processId": "makerCheckerRework",
			  "processName": "Maker Checker Rework Workflow",
			  "variables": [
			    {
			      "name": "approved",
			      "type": "Boolean"
			    }
			  ],
			  "nodes": [
			    {
			      "id": "start",
			      "type": "START_EVENT",
			      "name": "Start"
			    },
			    {
			      "id": "merge",
			      "type": "EXCLUSIVE_GATEWAY",
			      "name": "Merge",
			      "gatewayDirection": "Converging"
			    },
			    {
			      "id": "maker",
			      "type": "USER_TASK",
			      "name": "Maker"
			    },
			    {
			      "id": "checker",
			      "type": "USER_TASK",
			      "name": "Checker",
			      "inputs": [
			        {
			          "variable": "approved",
			          "type": "Boolean"
			        }
			      ],
			      "outputs": [
			        {
			          "variable": "approved",
			          "type": "Boolean"
			        }
			      ]
			    },
			    {
			      "id": "decision",
			      "type": "EXCLUSIVE_GATEWAY",
			      "name": "Approval Decision",
			      "gatewayDirection": "Diverging"
			      
			    },
			    {
			      "id": "end",
			      "type": "END_EVENT",
			      "name": "Completed"
			    }
			  ],
			  "connections": [
			    {
			      "source": "start",
			      "target": "merge"
			    },
			    {
			      "source": "merge",
			      "target": "maker"
			    },
			    {
			      "source": "maker",
			      "target": "checker"
			    },
			    {
			      "source": "checker",
			      "target": "decision"
			    },
			    {
			      "source": "decision",
			      "target": "end",
			      "name": "Approved",
			      "conditionExpression": "return approved;"
			    },
			    {
			      "source": "decision",
			      "target": "merge",
			      "name": "Rejected",
			      "defaultFlow": true
			    }
			  ]
			}
			""" ;

	static String parallel = """
					{
			    "processId": "parallelApproval",
			    "processName": "Parallel Approval Workflow",
			    "nodes": [
			      {
			        "id": "start",
			        "type": "START_EVENT",
			        "name": "Start"
			      },
			      {
			        "id": "fork",
			        "type": "PARALLEL_GATEWAY",
			        "name": "Fork",
			        "gatewayDirection": "Diverging"
			      },
			      {
			        "id": "verifyDocuments",
			        "type": "USER_TASK",
			        "name": "Verify Documents"
			      },
			      {
			        "id": "creditCheck",
			        "type": "USER_TASK",
			        "name": "Credit Check"
			      },
			      {
			        "id": "join",
			        "type": "PARALLEL_GATEWAY",
			        "name": "Join",
			        "gatewayDirection": "Converging"
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
			        "target": "fork"
			      },
			      {
			        "source": "fork",
			        "target": "verifyDocuments"
			      },
			      {
			        "source": "fork",
			        "target": "creditCheck"
			      },
			      {
			        "source": "verifyDocuments",
			        "target": "join"
			      },
			      {
			        "source": "creditCheck",
			        "target": "join"
			      },
			      {
			        "source": "join",
			        "target": "end"
			      }
			    ]
			  }
			""" ;

	public static void main(String[] args) {

		SpringApplication.run(BpmnGereratorApplication.class, args);


	}

}
