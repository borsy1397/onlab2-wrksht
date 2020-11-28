package com.captainborsy.wrksht.repository;

import com.captainborsy.wrksht.model.Status;
import com.captainborsy.wrksht.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, String> {

    List<Workflow> findByWorksheetId(String worksheetId);

    List<Workflow> findByWorksheetIdAndStatus(String worksheetId, Status status);
}
