package com.captainborsy.wrksht.controller;

import com.captainborsy.wrksht.api.UserApi;
import com.captainborsy.wrksht.api.model.UserDTO;
import com.captainborsy.wrksht.api.model.UserRegistrationDTO;
import com.captainborsy.wrksht.api.model.UserUpdateDTO;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.EntityNotFoundException;
import com.captainborsy.wrksht.mapper.UserMapper;
import com.captainborsy.wrksht.security.AuthoritiesConstants;
import com.captainborsy.wrksht.service.DocxCreatorService;
import com.captainborsy.wrksht.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final DocxCreatorService docxCreatorService;

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<Void> userForceLogout(String id) {
        userService.forceLogout(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<Void> userLogout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<UserDTO> createUser(@Valid UserRegistrationDTO userRegistrationDTO) {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO(userService.createUser(userRegistrationDTO)));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<Void> deleteMe() {
        userService.deleteCurrentUserProfile();
        return ResponseEntity.noContent().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<Void> deleteUserById(String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<UserDTO> getMe() {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO((userService.getCurrentUserProfile())));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD})
    public ResponseEntity<UserDTO> getUserById(String userId) {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO((userService.getUserById(userId))));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD})
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(UserMapper.mapUsersListToUsersDTOList((userService.getUsers())));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<Void> makeUserAdmin(String userId) {
        userService.makeAdmin(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<UserDTO> updateMe(@Valid UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO((userService.updateCurrentUserProfile(userUpdateDTO))));
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<UserDTO> updateUserById(String userId, @Valid UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(UserMapper.mapUserToUserDTO((userService.updateUser(userId, userUpdateDTO))));
    }
}
