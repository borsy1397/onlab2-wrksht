package com.captainborsy.wrksht.mapper;

import com.captainborsy.wrksht.api.model.StationDetailsDTO;
import com.captainborsy.wrksht.api.model.UserDTO;
import com.captainborsy.wrksht.model.Station;
import com.captainborsy.wrksht.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class StationMapper {

    private StationMapper() {}

    public static StationDetailsDTO mapStationToStationDetailsDTO(Station station) {
        return StationDetailsDTO.builder()
                .id(station.getId())
                .name(station.getName())
                .set(station.isSet())
                .loggedInUserId(station.getLoggedInUser() != null ? station.getLoggedInUser().getId() : null)
                //.createdAt(user.getCreatedAt().atOffset(ZoneOffset.UTC))
                //.updatedAt(user.getUpdatedAt().atOffset(ZoneOffset.UTC))
                .build();
    }

    public static List<StationDetailsDTO> mapStationListToStationDetailsDTOList(List<Station> stations) {
        return stations.stream().map(StationMapper::mapStationToStationDetailsDTO).collect(Collectors.toList());
    }
}
