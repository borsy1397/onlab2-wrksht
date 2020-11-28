package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.api.model.ListWorkflowCreationDTO;
import com.captainborsy.wrksht.api.model.OrderWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorksheetDTO;
import com.captainborsy.wrksht.api.model.WorkflowStatusChangeDTO;
import com.captainborsy.wrksht.api.model.WorksheetCreationDTO;
import com.captainborsy.wrksht.model.Workflow;
import com.captainborsy.wrksht.model.Worksheet;
import com.captainborsy.wrksht.service.WorksheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WorksheetServiceImpl implements WorksheetService {
    @Override
    public void changeWorkflowStatus(String worksheetId, String workflowId, WorkflowStatusChangeDTO workflowStatusChangeDTO) {

    }

    @Override
    public Workflow createWorkflowToWorksheet(String worksheetId, ListWorkflowCreationDTO listWorkflowCreationDTO) {
        return null;
    }

    @Override
    public Worksheet createWorksheet(WorksheetCreationDTO worksheetCreationDTO) {
        return null;
    }

    @Override
    public void deleteById(String worksheetId) {

    }

    @Override
    public Workflow getWorkflowById(String worksheetId, String workflowId) {
        return null;
    }

    @Override
    public List<Workflow> getWorkflowsByWorksheetId(String worksheetId, String status) {
        return null;
    }

    @Override
    public Worksheet getWorksheetById(String worksheetId) {
        return null;
    }

    @Override
    public Worksheet getWorksheets(String status) {
        return null;
    }

    @Override
    public void orderWorkflowById(String worksheetId, String workflowId, OrderWorkflowDTO orderWorkflowDTO) {

    }

    @Override
    public void updateWorkflowById(String worksheetId, String workflowId, UpdateWorkflowDTO updateWorkflowDTO) {

    }

    @Override
    public void updateWorksheetById(String worksheetId, UpdateWorksheetDTO updateWorksheetDTO) {

    }
}
