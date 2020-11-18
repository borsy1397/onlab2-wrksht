package com.captainborsy.wrksht.repository;

import com.captainborsy.wrksht.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, String> {
}
