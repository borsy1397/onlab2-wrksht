package com.captainborsy.wrksht.controller;

import com.captainborsy.wrksht.api.StationApi;
import com.captainborsy.wrksht.api.model.StationCreationDTO;
import com.captainborsy.wrksht.api.model.StationDetailsDTO;
import com.captainborsy.wrksht.api.model.WorkflowDetailsDTO;
import com.captainborsy.wrksht.mapper.StationMapper;
import com.captainborsy.wrksht.mapper.WorkflowMapper;
import com.captainborsy.wrksht.security.AuthoritiesConstants;
import com.captainborsy.wrksht.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StationController implements StationApi {

    private final StationService stationService;

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<StationDetailsDTO> createStation(@Valid StationCreationDTO stationCreationDTO) {
        return ResponseEntity.ok(StationMapper.mapStationToStationDetailsDTO(stationService.createStation(stationCreationDTO)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<Void> deleteStationById(String stationId) {
        stationService.deleteStationById(stationId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<List<StationDetailsDTO>> getAllStations() {
        return ResponseEntity.ok(StationMapper.mapStationListToStationDetailsDTOList(stationService.getAllStation()));
    }

    @Override
    public ResponseEntity<List<WorkflowDetailsDTO>> getWorkflowsOfStetion(String stationId) {
        return ResponseEntity.ok(WorkflowMapper.mapWorkflowListToWorkflowDetailsDTOList(stationService.getWorkflowsByStationId(stationId)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<StationDetailsDTO> getStationById(String stationId) {
        return ResponseEntity.ok(StationMapper.mapStationToStationDetailsDTO(stationService.getStationById(stationId)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<Void> setStation(String stationId) {
        stationService.setStation(stationId);
        return ResponseEntity.ok().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<Void> stationForceLogout(String stationId) {
        stationService.forceLogoutStation(stationId);
        return ResponseEntity.ok().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<Void> updateStationById(String stationId, @Valid StationCreationDTO stationCreationDTO) {
        stationService.updateStationById(stationId, stationCreationDTO);
        return ResponseEntity.ok().build();
    }
}
