package com.captainborsy.wrksht.controller;

import com.captainborsy.wrksht.api.WorksheetApi;
import com.captainborsy.wrksht.api.model.OrderWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorksheetDTO;
import com.captainborsy.wrksht.api.model.WorkflowCreationDTO;
import com.captainborsy.wrksht.api.model.WorkflowDetailsDTO;
import com.captainborsy.wrksht.api.model.WorkflowStatusChangeDTO;
import com.captainborsy.wrksht.api.model.WorksheetCreationDTO;
import com.captainborsy.wrksht.api.model.WorksheetDetailsDTO;
import com.captainborsy.wrksht.mapper.WorkflowMapper;
import com.captainborsy.wrksht.mapper.WorksheetMapper;
import com.captainborsy.wrksht.service.WorksheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorksheetController implements WorksheetApi {

    private final WorksheetService worksheetService;

    @Override
    public ResponseEntity<Void> changeWorkflowStatus(String worksheetId, String workflowId, @Valid WorkflowStatusChangeDTO workflowStatusChangeDTO) {
        worksheetService.changeWorkflowStatus(worksheetId, workflowId, workflowStatusChangeDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<WorkflowDetailsDTO> createWorkflowsForWorksheetByWorksheetId(String worksheetId, @Valid WorkflowCreationDTO workflowCreationDTO) {
        return ResponseEntity.ok(WorkflowMapper.mapWorkflowToWorkflowDetailsDTO(worksheetService.createWorkflowToWorksheet(worksheetId, workflowCreationDTO)));
    }

    @Override
    public ResponseEntity<WorksheetDetailsDTO> createWorksheet(@Valid WorksheetCreationDTO worksheetCreationDTO) {
        return ResponseEntity.ok(WorksheetMapper.mapWorksheetToWorksheetDetailsDTO(worksheetService.createWorksheet(worksheetCreationDTO)));
    }

    @Override
    public ResponseEntity<Void> deleteWorksheetById(String worksheetId) {
        worksheetService.deleteWorksheetById(worksheetId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteWorkflowById(String worksheetId, String workflowId) {
        worksheetService.deleteWorkflowById(worksheetId, workflowId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<WorkflowDetailsDTO> getWorkflowById(String worksheetId, String workflowId) {
        return ResponseEntity.ok(WorkflowMapper.mapWorkflowToWorkflowDetailsDTO(worksheetService.getWorkflowById(worksheetId, workflowId)));
    }

    @Override
    public ResponseEntity<List<WorkflowDetailsDTO>> getWorkflowsByWorksheetId(String worksheetId, @Valid String status) {
        return ResponseEntity.ok(WorkflowMapper.mapWorkflowListToWorkflowDetailsDTOList(worksheetService.getWorkflowsByWorksheetId(worksheetId, status)));
    }

    @Override
    public ResponseEntity<WorksheetDetailsDTO> getWorksheetById(String worksheetId) {
        return ResponseEntity.ok(WorksheetMapper.mapWorksheetToWorksheetDetailsDTO(worksheetService.getWorksheetById(worksheetId)));
    }

    @Override
    public ResponseEntity<List<WorksheetDetailsDTO>> getWorksheets(@Valid String status) {
        return ResponseEntity.ok(WorksheetMapper.mapWorksheetListToWorksheetDetailsDTOList(worksheetService.getWorksheets(status)));
    }

    @Override
    public ResponseEntity<Void> orderWorkflowById(String worksheetId, String workflowId, @Valid OrderWorkflowDTO orderWorkflowDTO) {
        worksheetService.orderWorkflowById(worksheetId, workflowId, orderWorkflowDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateWorkflowById(String worksheetId, String workflowId, @Valid UpdateWorkflowDTO updateWorkflowDTO) {
        worksheetService.updateWorkflowById(worksheetId, workflowId, updateWorkflowDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateWorksheetById(String worksheetId, @Valid UpdateWorksheetDTO updateWorksheetDTO) {
        worksheetService.updateWorksheetById(worksheetId, updateWorksheetDTO);
        return ResponseEntity.ok().build();
    }
}
