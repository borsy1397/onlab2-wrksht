package com.captainborsy.wrksht.mapper;

import com.captainborsy.wrksht.api.model.StationDetailsDTO;
import com.captainborsy.wrksht.model.Station;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class StationMapper {

    private StationMapper() {
    }

    public static StationDetailsDTO mapStationToStationDetailsDTO(Station station) {
        return StationDetailsDTO.builder()
                .id(station.getId())
                .name(station.getName())
                .set(station.isSet())
                .deleted(station.isDeleted())
                .loggedInUserId(station.getLoggedInUser() != null ? station.getLoggedInUser().getId() : null)
                .createdAt(station.getCreatedAt().atOffset(ZoneOffset.UTC))
                .updatedAt(station.getUpdatedAt().atOffset(ZoneOffset.UTC))
                .build();
    }

    public static List<StationDetailsDTO> mapStationListToStationDetailsDTOList(List<Station> stations) {
        return stations.stream().map(StationMapper::mapStationToStationDetailsDTO).collect(Collectors.toList());
    }
}
