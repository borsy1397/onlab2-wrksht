package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.api.model.StationCreationDTO;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.ConflictException;
import com.captainborsy.wrksht.errorhandling.exception.EntityNotFoundException;
import com.captainborsy.wrksht.model.Station;
import com.captainborsy.wrksht.model.Workflow;
import com.captainborsy.wrksht.repository.StationRepository;
import com.captainborsy.wrksht.repository.WorkflowRepository;
import com.captainborsy.wrksht.service.StationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final WorkflowRepository workflowRepository;

    @Override
    @Transactional
    public Station createStation(StationCreationDTO stationCreationDTO) {

        if (stationRepository.findByName(stationCreationDTO.getName()).isPresent()) {
            throw new ConflictException("Station name is already registered", WrkshtErrors.CONFLICT);
        }

        Station station = Station.builder().name(stationCreationDTO.getName()).build();

        return stationRepository.save(station);
    }

    @Override
    @Transactional
    public void deleteStationById(String stationId) {
        Station station = getStationById(stationId);
        station.setDeleted(true);
    }

    @Override
    @Transactional
    public List<Station> getAllStation() {
        return stationRepository.findAllByIsDeleted(false);
    }

    @Override
    @Transactional
    public Station getStationById(String stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new EntityNotFoundException("Station not found", WrkshtErrors.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public void setStation(String stationId) {
        Station station = getStationById(stationId);
        station.setSet(true);
    }

    @Override
    @Transactional
    public void forceLogoutStation(String stationId) {
        Station station = getStationById(stationId);
        station.setSet(false);
    }

    @Override
    public List<Workflow> getWorkflowsByStationId(String stationId) {
        return workflowRepository.findByStationId(stationId);
    }

    @Override
    @Transactional
    public void updateStationById(String stationId, StationCreationDTO stationCreationDTO) {
        Station station = getStationById(stationId);

        if (stationCreationDTO.getName() != null) {
            if (stationRepository.findByNameAndIdNot(stationCreationDTO.getName(), stationId).isPresent()) {
                throw new ConflictException("Station name is already registered", WrkshtErrors.CONFLICT);
            }
            station.setName(stationCreationDTO.getName());
        }
    }
}
