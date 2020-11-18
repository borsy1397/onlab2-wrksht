package com.captainborsy.wrksht.controller;

import com.captainborsy.wrksht.api.UserApi;
import com.captainborsy.wrksht.api.model.UserDTO;
import com.captainborsy.wrksht.api.model.UserRegistrationDTO;
import com.captainborsy.wrksht.api.model.UserUpdateDTO;
import com.captainborsy.wrksht.mapper.UserMapper;
import com.captainborsy.wrksht.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<Void> userForceLogout(String id) {
        userService.forceLogout(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> userLogout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserDTO> createUser(@Valid UserRegistrationDTO userRegistrationDTO) {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO(userService.createUser(userRegistrationDTO)));
    }

    @Override
    public ResponseEntity<Void> deleteMe() {
        userService.deleteCurrentUserProfile();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteUserById(String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserDTO> getMe() {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO((userService.getCurrentUserProfile())));
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(String userId) {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO((userService.getUserById(userId))));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(UserMapper.mapUsersListToUsersDTOList((userService.getUsers())));
    }

    @Override
    public ResponseEntity<Void> makeUserAdmin(String userId) {
        userService.makeAdmin(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserDTO> updateMe(@Valid UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO((userService.updateCurrentUserProfile(userUpdateDTO))));
    }

    @Override
    public ResponseEntity<UserDTO> updateUserById(String userId, @Valid UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO((userService.updateUser(userId, userUpdateDTO))));
    }
}
