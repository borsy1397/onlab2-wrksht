package com.captainborsy.wrksht.repository;

import com.captainborsy.wrksht.model.Worksheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorksheetRepository extends JpaRepository<Worksheet, String> {
}
