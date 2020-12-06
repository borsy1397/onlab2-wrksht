package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.api.model.OrderWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorkflowDTO;
import com.captainborsy.wrksht.api.model.UpdateWorksheetDTO;
import com.captainborsy.wrksht.api.model.WorkflowCreationDTO;
import com.captainborsy.wrksht.api.model.WorkflowStatusChangeDTO;
import com.captainborsy.wrksht.api.model.WorksheetCreationDTO;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.EntityNotFoundException;
import com.captainborsy.wrksht.errorhandling.exception.ExportException;
import com.captainborsy.wrksht.errorhandling.exception.InvalidOperationException;
import com.captainborsy.wrksht.errorhandling.exception.UnprocessableEntityException;
import com.captainborsy.wrksht.errorhandling.exception.WorkflowStatusChangingException;
import com.captainborsy.wrksht.mapper.WorkflowMapper;
import com.captainborsy.wrksht.mapper.WorksheetMapper;
import com.captainborsy.wrksht.model.Station;
import com.captainborsy.wrksht.model.Status;
import com.captainborsy.wrksht.model.User;
import com.captainborsy.wrksht.model.Workflow;
import com.captainborsy.wrksht.model.Worksheet;
import com.captainborsy.wrksht.repository.WorkflowRepository;
import com.captainborsy.wrksht.repository.WorksheetRepository;
import com.captainborsy.wrksht.service.DocxCreatorService;
import com.captainborsy.wrksht.service.StationService;
import com.captainborsy.wrksht.service.UserService;
import com.captainborsy.wrksht.service.WorksheetService;
import com.captainborsy.wrksht.service.export.DocxTableRowWorkflow;
import com.captainborsy.wrksht.service.export.DocxWorksheet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.captainborsy.wrksht.service.impl.DocxCreatorServiceImpl.DATE_TIME_FORMATTER;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorksheetServiceImpl implements WorksheetService {

    private static final String WORKFLOW_NOT_FOUND = "Workflow not found";
    private static final String WORKSHEET_NOT_FOUND = "Worksheet not found";

    private final WorksheetRepository worksheetRepository;
    private final WorkflowRepository workflowRepository;
    private final UserService userService;
    private final StationService stationService;
    private final DocxCreatorService docxCreatorService;

    @Override
    @Transactional
    public void changeWorkflowStatus(String worksheetId, String workflowId, WorkflowStatusChangeDTO workflowStatusChangeDTO) {
        Workflow workflow = getWorkflowById(worksheetId, workflowId);
        Worksheet worksheet = getWorksheetById(worksheetId);


        if (workflow.getStatus() == Status.TODO && workflowStatusChangeDTO.getToStatus().equals("START")) {
            User user = userService.getCurrentUser();

            workflow.setStartedAt(Instant.now());
            workflow.setStatus(Status.IN_PROGRESS);
            workflow.setWorker(user);
            user.getWorkflows().add(workflow);

            worksheet.setStatus(Status.IN_PROGRESS);

            workflowRepository.save(workflow);

            return;
        }

        if (workflow.getStatus() == Status.IN_PROGRESS && workflowStatusChangeDTO.getToStatus().equals("STOP")) {

            workflow.setStoppedAt(Instant.now());
            workflow.setStatus(Status.DONE);

            List<Workflow> workflows = workflowRepository.findByWorksheetId(worksheetId);

            boolean isFinishedWorksheet = true;

            for (Workflow w : workflows) {
                if (w.getStatus() != Status.DONE && !w.getId().equals(workflowId)) {
                    isFinishedWorksheet = false;
                    break;
                }
            }

            if (isFinishedWorksheet) {
                worksheet.setStatus(Status.DONE);
            }

            return;
        }

        throw new WorkflowStatusChangingException("Workflow status (" + workflow.getStatus() + ") and change status (" + workflowStatusChangeDTO.getToStatus() + ") are not compatible", WrkshtErrors.STATUS_CHANGE_ERROR);
    }

    @Override
    @Transactional
    public Workflow createWorkflowToWorksheet(String worksheetId, WorkflowCreationDTO workflowCreationDTO) {
        Worksheet worksheet = getWorksheetById(worksheetId);
        Station station = stationService.getStationById(workflowCreationDTO.getStationId());
        Workflow workflow = WorkflowMapper.mapWorkflowCreationDTOtoWorkflow(workflowCreationDTO);

        List<Workflow> workflows = worksheet.getWorkflows();
        workflows.sort(Comparator.comparing(Workflow::getOrdering));

        workflow.setStatus(Status.TODO);
        workflow.setOrdering(workflows.size() + 1);
        workflow.setStation(station);
        station.getWorkflows().add(workflow);
        worksheet.getWorkflows().add(workflow);
        workflow.setWorksheet(worksheet);


        return workflowRepository.save(workflow);
    }

    @Override
    @Transactional
    public Worksheet createWorksheet(WorksheetCreationDTO worksheetCreationDTO) {
        Worksheet worksheet = WorksheetMapper.mapWorksheetCreationDTOtoWorksheet(worksheetCreationDTO);
        User user = userService.getCurrentUser();

        if (Instant.now().isAfter(worksheet.getDeadline())) {
            throw new UnprocessableEntityException("Deadline is after actual time", WrkshtErrors.DEADLINE_ERROR);
        }

        worksheet.setStatus(Status.TODO);
        worksheet.setCreator(user);
        user.getCreatedWorksheets().add(worksheet);

        return worksheetRepository.save(worksheet);
    }

    @Override
    @Transactional
    public void deleteWorksheetById(String worksheetId) {
        Worksheet worksheet = getWorksheetById(worksheetId);
        worksheet.setDeleted(true);
    }

    @Override
    @Transactional
    public Workflow getWorkflowById(String worksheetId, String workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow(() -> new EntityNotFoundException(WORKFLOW_NOT_FOUND, WrkshtErrors.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public List<Workflow> getWorkflowsByWorksheetId(String worksheetId, String status) {

        if (status == null) {
            return workflowRepository.findByWorksheetId(worksheetId);
        }

        Status parsedStatus = parseStatus(status);

        return workflowRepository.findByWorksheetIdAndStatus(worksheetId, parsedStatus);
    }

    @Override
    @Transactional
    public Worksheet getWorksheetById(String worksheetId) {
        return worksheetRepository.findById(worksheetId).orElseThrow(() -> new EntityNotFoundException(WORKSHEET_NOT_FOUND, WrkshtErrors.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public List<Worksheet> getWorksheets(String status) {

        if (status == null) {
            return worksheetRepository.findByIsDeleted(false);
        }

        Status parsedStatus = parseStatus(status);

        return worksheetRepository.findByStatusAndIsDeleted(parsedStatus, false);
    }

    @Override
    @Transactional
    public void orderWorkflowById(String worksheetId, String workflowId, OrderWorkflowDTO orderWorkflowDTO) {
        List<Workflow> workflows = workflowRepository.findByWorksheetId(worksheetId);
        workflows.sort(Comparator.comparing(Workflow::getOrdering));

        for (int i = 0; i < workflows.size(); i++) {
            Workflow actual = workflows.get(i);
            int actualOrder = actual.getOrdering();
            if (actual.getId().equals(workflowId)) {
                if (orderWorkflowDTO.getDirection() == OrderWorkflowDTO.DirectionEnum.DOWN) {
                    if (actualOrder != 1) {
                        actual.setOrdering(actualOrder - 1);
                        workflows.get(i - 1).setOrdering(actualOrder);
                    }
                } else {
                    if (actualOrder != workflows.size()) {
                        actual.setOrdering(actualOrder + 1);
                        workflows.get(i + 1).setOrdering(actualOrder);
                    }
                }
                break;
            }
        }
    }

    @Override
    @Transactional
    public void export(String worksheetId, ByteArrayOutputStream os) {
        Worksheet worksheet = getWorksheetById(worksheetId);

        DocxWorksheet docxWorksheet = DocxWorksheet.builder()
                .name(worksheet.getName())
                .productName(worksheet.getProductName())
                .quantity(worksheet.getQuantity().toString())
                .customer(worksheet.getCustomer())
                .orderNumber(worksheet.getOrderNumber())
                .deadline(DATE_TIME_FORMATTER.format(worksheet.getDeadline().truncatedTo(ChronoUnit.MINUTES)))
                .status(worksheet.getStatus().getDocxStatus())
                .createdAt(DATE_TIME_FORMATTER.format(worksheet.getCreatedAt().truncatedTo(ChronoUnit.MINUTES)))
                .isDeleted(worksheet.isDeleted() ? "A munkalap TÖRÖLVE lett" : "")
                .creator(worksheet.getCreator().getLastName() + " " + worksheet.getCreator().getFirstName())
                .build();

        List<Workflow> workflows = worksheet.getWorkflows();

        if (workflows.isEmpty()) {
            try {
                XWPFDocument document = new XWPFDocument();
                document.write(os);
            } catch (IOException e) {
                throw new ExportException("Error while exporting", WrkshtErrors.EXPORT_ERROR);
            }
        }

        workflows.sort(Comparator.comparing(Workflow::getOrdering));

        DocxTableRowWorkflow firstRow = new DocxTableRowWorkflow("Név", "Megjegyzés", "Állapot", "Kezdés ideje");
        List<DocxTableRowWorkflow> docxTableRowWorkflows = workflows.stream().map(this::mapToDocxTableRowWorkflow).collect(Collectors.toList());

        docxTableRowWorkflows.add(0, firstRow);

        docxWorksheet.setDocxTableRowWorkflows(docxTableRowWorkflows);

        docxCreatorService.export(docxWorksheet, os);
    }

    @Override
    @Transactional
    public void deleteWorkflowById(String worksheetId, String workflowId) {
        Workflow workflow = getWorkflowById(worksheetId, workflowId);

        if (workflow.getStatus() != Status.TODO) {
            throw new InvalidOperationException("Workflow has started or the status is DONE", WrkshtErrors.DELETED_WORKFLOW);
        }

        workflowRepository.delete(workflow);
    }

    @Override
    @Transactional
    public void updateWorksheetById(String worksheetId, UpdateWorksheetDTO updateWorksheetDTO) {
        Worksheet worksheet = getWorksheetById(worksheetId);

        if (worksheet.getStatus() == Status.DONE) {
            throw new InvalidOperationException("Finished worksheet can not be updated", WrkshtErrors.WORKSHEET_ALREADY_FINISHED);
        }

        if (updateWorksheetDTO.getComment() != null) {
            worksheet.setComment(updateWorksheetDTO.getComment());
        }

        if (updateWorksheetDTO.getCustomer() != null) {
            worksheet.setCustomer(updateWorksheetDTO.getCustomer());
        }

        if (updateWorksheetDTO.getDeadLine() != null && updateWorksheetDTO.getDeadLine().toInstant().isAfter(Instant.now())) {
            worksheet.setDeadline(updateWorksheetDTO.getDeadLine().toInstant());
        }

        if (updateWorksheetDTO.getName() != null) {
            worksheet.setName(updateWorksheetDTO.getName());
        }

        if (updateWorksheetDTO.getOrderNumber() != null) {
            worksheet.setOrderNumber(updateWorksheetDTO.getOrderNumber());
        }

        if (updateWorksheetDTO.getProductName() != null) {
            worksheet.setProductName(updateWorksheetDTO.getProductName());
        }

        if (updateWorksheetDTO.getQuantity() != null) {
            worksheet.setQuantity(updateWorksheetDTO.getQuantity());
        }

    }

    @Override
    @Transactional
    public void updateWorkflowById(String worksheetId, String workflowId, UpdateWorkflowDTO updateWorkflowDTO) {
        Workflow workflow = getWorkflowById(worksheetId, workflowId);

        if (workflow.getStatus() == Status.DONE) {
            throw new InvalidOperationException("Finished workflow can not be updated", WrkshtErrors.WORKFLOW_ALREADY_FINISHED);
        }

        if (updateWorkflowDTO.getName() != null) {
            workflow.setName(updateWorkflowDTO.getName());
        }
        if (updateWorkflowDTO.getShiftLeadComment() != null) {
            workflow.setShiftLeadComment(updateWorkflowDTO.getShiftLeadComment());
        }

        if (updateWorkflowDTO.getStationId() != null && !updateWorkflowDTO.getStationId().equals("")) {
            if (workflow.getStatus() == Status.TODO) {
                Station station = stationService.getStationById(updateWorkflowDTO.getStationId());
                workflow.getStation().getWorkflows().remove(workflow);
                workflow.setStation(station);
                workflowRepository.save(workflow);
            } else {
                throw new InvalidOperationException("Workflow is in progress can not be updated", WrkshtErrors.WORKFLOW_IN_PROGRESS);
            }
        }
    }

    private Status parseStatus(String status) {
        Status parsedStatus;
        try {
            parsedStatus = Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new UnprocessableEntityException("Parsing status (" + status + ") was failed", WrkshtErrors.PARSE_ERROR);
        }
        return parsedStatus;
    }

    private DocxTableRowWorkflow mapToDocxTableRowWorkflow(Workflow workflow) {
        return new DocxTableRowWorkflow(workflow.getName(), workflow.getShiftLeadComment(), workflow.getStatus().getDocxStatus(), workflow.getStartedAt() != null ? DATE_TIME_FORMATTER.format(workflow.getStartedAt()) : "-");
    }

}
