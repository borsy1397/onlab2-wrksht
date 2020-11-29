package com.captainborsy.wrksht.repository;

import com.captainborsy.wrksht.model.Status;
import com.captainborsy.wrksht.model.Worksheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorksheetRepository extends JpaRepository<Worksheet, String> {
    List<Worksheet> findByStatusAndIsDeleted(Status status, boolean isDeleted);

    List<Worksheet> findByIsDeleted(boolean isDeleted);
}
