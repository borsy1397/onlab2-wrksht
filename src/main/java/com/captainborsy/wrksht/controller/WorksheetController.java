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
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.EntityNotFoundException;
import com.captainborsy.wrksht.errorhandling.exception.UnprocessableEntityException;
import com.captainborsy.wrksht.mapper.WorkflowMapper;
import com.captainborsy.wrksht.mapper.WorksheetMapper;
import com.captainborsy.wrksht.security.AuthoritiesConstants;
import com.captainborsy.wrksht.service.WorksheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorksheetController implements WorksheetApi {

    private final WorksheetService worksheetService;

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<Void> changeWorkflowStatus(String worksheetId, String workflowId, @Valid WorkflowStatusChangeDTO workflowStatusChangeDTO) {
        worksheetService.changeWorkflowStatus(worksheetId, workflowId, workflowStatusChangeDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD})
    public ResponseEntity<WorkflowDetailsDTO> createWorkflowsForWorksheetByWorksheetId(String worksheetId, @Valid WorkflowCreationDTO workflowCreationDTO) {
        return ResponseEntity.ok(WorkflowMapper.mapWorkflowToWorkflowDetailsDTO(worksheetService.createWorkflowToWorksheet(worksheetId, workflowCreationDTO)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD})
    public ResponseEntity<WorksheetDetailsDTO> createWorksheet(@Valid WorksheetCreationDTO worksheetCreationDTO) {
        return ResponseEntity.ok(WorksheetMapper.mapWorksheetToWorksheetDetailsDTO(worksheetService.createWorksheet(worksheetCreationDTO)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD})
    public ResponseEntity<Void> deleteWorksheetById(String worksheetId) {
        worksheetService.deleteWorksheetById(worksheetId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Resource> exportWorksheet(String worksheetId) {
        String filename = "export_worksheet.docx";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            worksheetService.export(worksheetId, os);
            ByteArrayResource resource = new ByteArrayResource(os.toByteArray());
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new UnprocessableEntityException("Worksheet export has failed", WrkshtErrors.USER_ALREADY_LOGGED_IN);
        }
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD})
    public ResponseEntity<Void> deleteWorkflowById(String worksheetId, String workflowId) {
        worksheetService.deleteWorkflowById(worksheetId, workflowId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<WorkflowDetailsDTO> getWorkflowById(String worksheetId, String workflowId) {
        return ResponseEntity.ok(WorkflowMapper.mapWorkflowToWorkflowDetailsDTO(worksheetService.getWorkflowById(worksheetId, workflowId)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<List<WorkflowDetailsDTO>> getWorkflowsByWorksheetId(String worksheetId, @Valid String status) {
        return ResponseEntity.ok(WorkflowMapper.mapWorkflowListToWorkflowDetailsDTOList(worksheetService.getWorkflowsByWorksheetId(worksheetId, status)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<WorksheetDetailsDTO> getWorksheetById(String worksheetId) {
        return ResponseEntity.ok(WorksheetMapper.mapWorksheetToWorksheetDetailsDTO(worksheetService.getWorksheetById(worksheetId)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<List<WorksheetDetailsDTO>> getWorksheets(@Valid String status) {
        return ResponseEntity.ok(WorksheetMapper.mapWorksheetListToWorksheetDetailsDTOList(worksheetService.getWorksheets(status)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<Void> orderWorkflowById(String worksheetId, String workflowId, @Valid OrderWorkflowDTO orderWorkflowDTO) {
        worksheetService.orderWorkflowById(worksheetId, workflowId, orderWorkflowDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<Void> updateWorkflowById(String worksheetId, String workflowId, @Valid UpdateWorkflowDTO updateWorkflowDTO) {
        worksheetService.updateWorkflowById(worksheetId, workflowId, updateWorkflowDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<Void> updateWorksheetById(String worksheetId, @Valid UpdateWorksheetDTO updateWorksheetDTO) {
        worksheetService.updateWorksheetById(worksheetId, updateWorksheetDTO);
        return ResponseEntity.ok().build();
    }
}
