package com.captainborsy.wrksht.mapper;

import com.captainborsy.wrksht.api.model.WorkflowCreationDTO;
import com.captainborsy.wrksht.api.model.WorkflowDetailsDTO;
import com.captainborsy.wrksht.model.Workflow;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class WorkflowMapper {

    private WorkflowMapper() {
    }

    public static WorkflowDetailsDTO mapWorkflowToWorkflowDetailsDTO(Workflow workflow) {
        return WorkflowDetailsDTO.builder()
                .id(workflow.getId())
                .name(workflow.getName())
                .shiftLeadComment(workflow.getShiftLeadComment())
                .workerId(workflow.getWorker() != null ? workflow.getWorker().getId() : null)
                .worksheetId(workflow.getWorksheet().getId())
                .stationId(workflow.getStation().getId())
                .status(WorkflowDetailsDTO.StatusEnum.valueOf(workflow.getStatus().toString()))
                .startedAt(workflow.getStartedAt() != null ? workflow.getStartedAt().atOffset(ZoneOffset.UTC) : null)
                .stoppedAt(workflow.getStoppedAt() != null ? workflow.getStoppedAt().atOffset(ZoneOffset.UTC) : null)
                .createdAt(workflow.getCreatedAt().atOffset(ZoneOffset.UTC))
                .updatedAt(workflow.getUpdatedAt().atOffset(ZoneOffset.UTC))
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
