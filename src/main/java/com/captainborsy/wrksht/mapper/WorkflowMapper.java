package com.captainborsy.wrksht.mapper;

import com.captainborsy.wrksht.api.model.StationDetailsDTO;
import com.captainborsy.wrksht.api.model.WorkflowCreationDTO;
import com.captainborsy.wrksht.api.model.WorkflowDetailsDTO;
import com.captainborsy.wrksht.model.Station;
import com.captainborsy.wrksht.model.Workflow;

import java.util.List;
import java.util.stream.Collectors;

public class WorkflowMapper {

    private WorkflowMapper() {}

    public static WorkflowDetailsDTO mapWorkflowToWorkflowDetailsDTO(Workflow workflow) {
        return WorkflowDetailsDTO.builder()
                .id(workflow.getId())
                .name(workflow.getName())
                //.createdAt(user.getCreatedAt().atOffset(ZoneOffset.UTC))
                //.updatedAt(user.getUpdatedAt().atOffset(ZoneOffset.UTC))
                .build();
    }

    public static List<WorkflowDetailsDTO> mapWorkflowListToWorkflowDetailsDTOList(List<Workflow> workflows) {
        return workflows.stream().map(WorkflowMapper::mapWorkflowToWorkflowDetailsDTO).collect(Collectors.toList());
    }

    public static Workflow mapWorkflowCreationDTOtoWorkflow(WorkflowCreationDTO workflowCreationDTO) {
        return Workflow.builder()
                .name(workflowCreationDTO.getName())
                .shiftLeadComment(workflowCreationDTO.getName())
                .build();
    }
}
