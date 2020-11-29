package com.captainborsy.wrksht.service;

import com.captainborsy.wrksht.api.model.OrderWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorksheetDTO;
import com.captainborsy.wrksht.api.model.WorkflowCreationDTO;
import com.captainborsy.wrksht.api.model.WorkflowStatusChangeDTO;
import com.captainborsy.wrksht.api.model.WorksheetCreationDTO;
import com.captainborsy.wrksht.model.Workflow;
import com.captainborsy.wrksht.model.Worksheet;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface WorksheetService {
    void changeWorkflowStatus(String worksheetId, String workflowId, WorkflowStatusChangeDTO workflowStatusChangeDTO);

    Workflow createWorkflowToWorksheet(String worksheetId, WorkflowCreationDTO workflowCreationDTO);

    Worksheet createWorksheet(WorksheetCreationDTO worksheetCreationDTO);

    void deleteWorksheetById(String worksheetId);

    Workflow getWorkflowById(String worksheetId, String workflowId);

    List<Workflow> getWorkflowsByWorksheetId(String worksheetId, String status);

    Worksheet getWorksheetById(String worksheetId);

    List<Worksheet> getWorksheets(String status);

    void orderWorkflowById(String worksheetId, String workflowId, OrderWorkflowDTO orderWorkflowDTO);

    void updateWorkflowById(String worksheetId, String workflowId, UpdateWorkflowDTO updateWorkflowDTO);

    void updateWorksheetById(String worksheetId, UpdateWorksheetDTO updateWorksheetDTO);

    void deleteWorkflowById(String worksheetId, String workflowId);

    void export(String worksheetId, ByteArrayOutputStream os);
}
