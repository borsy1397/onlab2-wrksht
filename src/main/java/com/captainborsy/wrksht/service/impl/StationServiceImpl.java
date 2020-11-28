package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.api.model.StationCreationDTO;
import com.captainborsy.wrksht.model.Station;
import com.captainborsy.wrksht.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StationServiceImpl implements StationService {
    @Override
    public Station createStation(StationCreationDTO stationCreationDTO) {
        return null;
    }

    @Override
    public void deleteStationById(String stationId) {

    }

    @Override
    public List<Station> getAllStation() {
        return null;
    }

    @Override
    public Station getStationById(String stationId) {

        // TODO check is deleted
        return null;
    }

    @Override
    public void setStation(String stationId) {

    }

    @Override
    public void forceLogoutStation(String stationId) {

    }

    @Override
    public void updateStationById(String stationId, StationCreationDTO stationCreationDTO) {

    }
}
