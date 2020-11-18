package com.captainborsy.wrksht.repository;

import com.captainborsy.wrksht.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
}
