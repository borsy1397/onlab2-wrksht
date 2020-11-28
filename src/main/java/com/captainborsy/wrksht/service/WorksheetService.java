package com.captainborsy.wrksht.service;

import com.captainborsy.wrksht.api.model.ListWorkflowCreationDTO;
import com.captainborsy.wrksht.api.model.OrderWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorksheetDTO;
import com.captainborsy.wrksht.api.model.WorkflowStatusChangeDTO;
import com.captainborsy.wrksht.api.model.WorksheetCreationDTO;
import com.captainborsy.wrksht.model.Workflow;
import com.captainborsy.wrksht.model.Worksheet;

import java.util.List;

public interface WorksheetService {
    void changeWorkflowStatus(String worksheetId, String workflowId, WorkflowStatusChangeDTO workflowStatusChangeDTO);

    Workflow createWorkflowToWorksheet(String worksheetId, ListWorkflowCreationDTO listWorkflowCreationDTO);

    Worksheet createWorksheet(WorksheetCreationDTO worksheetCreationDTO);

    void deleteById(String worksheetId);

    Workflow getWorkflowById(String worksheetId, String workflowId);

    List<Workflow> getWorkflowsByWorksheetId(String worksheetId, String status);

    Worksheet getWorksheetById(String worksheetId);

    Worksheet getWorksheets(String status);

    void orderWorkflowById(String worksheetId, String workflowId, OrderWorkflowDTO orderWorkflowDTO);

    void updateWorkflowById(String worksheetId, String workflowId, UpdateWorkflowDTO updateWorkflowDTO);

    void updateWorksheetById(String worksheetId, UpdateWorksheetDTO updateWorksheetDTO);
}
