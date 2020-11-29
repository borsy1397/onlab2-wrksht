package com.captainborsy.wrksht.repository;

import com.captainborsy.wrksht.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {

    Optional<Station> findByName(String name);

    Optional<Station> findByNameAndIdNot(String name, String id);

    List<Station> findAllByIsDeleted(boolean deleted);
}
