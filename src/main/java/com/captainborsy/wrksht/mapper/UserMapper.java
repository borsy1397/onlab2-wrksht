package com.captainborsy.wrksht.mapper;

import com.captainborsy.wrksht.api.model.UserDTO;
import com.captainborsy.wrksht.api.model.UserRegistrationDTO;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.UnprocessableEntityException;
import com.captainborsy.wrksht.model.Role;
import com.captainborsy.wrksht.model.User;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO mapUserToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .deleted(user.isDeleted())
                .username(user.getUsername())
                .role(UserDTO.RoleEnum.valueOf(user.getRole().toString()))
                .createdAt(user.getCreatedAt().atOffset(ZoneOffset.UTC))
                .updatedAt(user.getUpdatedAt().atOffset(ZoneOffset.UTC))
                .build();
    }

    public static List<UserDTO> mapUsersListToUsersDTOList(List<User> users) {
        return users.stream().map(UserMapper::mapUserToUserDTO).collect(Collectors.toList());
    }

    public static User mapUserRegistrationDTOToUser(UserRegistrationDTO userRegistrationDTO) {

        Role role;
        try {
            role = Role.valueOf(userRegistrationDTO.getRole());
        } catch (IllegalArgumentException e) {
            throw new UnprocessableEntityException("Parsing role (" + userRegistrationDTO.getRole() + ") was failed", WrkshtErrors.PARSE_ERROR);
        }

        return User.builder()
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .username(userRegistrationDTO.getUsername())
                .password(userRegistrationDTO.getPassword())
                .role(role)
                .build();
    }
}
