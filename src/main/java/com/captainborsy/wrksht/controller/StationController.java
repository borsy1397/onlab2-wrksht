package com.captainborsy.wrksht.controller;

import com.captainborsy.wrksht.api.StationApi;
import com.captainborsy.wrksht.api.model.StationCreationDTO;
import com.captainborsy.wrksht.api.model.StationDetailsDTO;
import com.captainborsy.wrksht.mapper.StationMapper;
import com.captainborsy.wrksht.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StationController implements StationApi {

    private final StationService stationService;

    @Override
    public ResponseEntity<StationDetailsDTO> createStation(@Valid StationCreationDTO stationCreationDTO) {
        return ResponseEntity.ok(StationMapper.mapStationToStationDetailsDTO(stationService.createStation(stationCreationDTO)));
    }

    @Override
    public ResponseEntity<Void> deleteStationById(String stationId) {
        stationService.deleteStationById(stationId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<StationDetailsDTO>> getAllStations() {
        return ResponseEntity.ok(StationMapper.mapStationListToStationDetailsDTOList(stationService.getAllStation()));
    }

    @Override
    public ResponseEntity<StationDetailsDTO> getStationById(String stationId) {
        return ResponseEntity.ok(StationMapper.mapStationToStationDetailsDTO(stationService.getStationById(stationId)));
    }

    @Override
    public ResponseEntity<Void> setStation(String stationId) {
        stationService.setStation(stationId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> stationForceLogout(String stationId) {
        stationService.forceLogoutStation(stationId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateStationById(String stationId, @Valid StationCreationDTO stationCreationDTO) {
        stationService.updateStationById(stationId, stationCreationDTO);
        return ResponseEntity.ok().build();
    }
}
