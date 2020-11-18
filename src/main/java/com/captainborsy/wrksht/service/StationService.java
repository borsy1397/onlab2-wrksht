package com.captainborsy.wrksht.service;

import com.captainborsy.wrksht.api.model.StationCreationDTO;
import com.captainborsy.wrksht.model.Station;

import java.util.List;

public interface StationService {
    Station createStation(StationCreationDTO stationCreationDTO);

    void deleteStationById(String stationId);

    List<Station> getAllStation();

    Station getStationById(String stationId);

    void setStation(String stationId);

    void forceLogoutStation(String stationId);

    void updateStationById(String stationId, StationCreationDTO stationCreationDTO);
}
