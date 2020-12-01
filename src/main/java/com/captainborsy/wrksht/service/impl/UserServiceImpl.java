package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.api.model.UserRegistrationDTO;
import com.captainborsy.wrksht.api.model.UserUpdateDTO;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.ConflictException;
import com.captainborsy.wrksht.errorhandling.exception.EntityNotFoundException;
import com.captainborsy.wrksht.errorhandling.exception.InvalidOperationException;
import com.captainborsy.wrksht.errorhandling.exception.NoUserInContextException;
import com.captainborsy.wrksht.mapper.UserMapper;
import com.captainborsy.wrksht.model.Role;
import com.captainborsy.wrksht.model.User;
import com.captainborsy.wrksht.repository.UserRepository;
import com.captainborsy.wrksht.security.AuthUserDetails;
import com.captainborsy.wrksht.security.AuthenticationFacade;
import com.captainborsy.wrksht.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String NOT_LOGGED_IN = "There is not logged in user";
    private static final String USERNAME_REGISTERED = "Username is already registered";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;

    // TODO user statisztikák

    @Override
    @Transactional
    public User createUser(UserRegistrationDTO newUser) {
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new ConflictException(USERNAME_REGISTERED, WrkshtErrors.CONFLICT);
        }

        User user = UserMapper.mapUserRegistrationDTOToUser(newUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User getCurrentUserProfile() {
        return getCurrentUser();
    }

    @Override
    @Transactional
    public User updateCurrentUserProfile(UserUpdateDTO profileDTO) {
        User updatedUser = fillUpdateUser(getCurrentUser(), profileDTO);
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteCurrentUserProfile() {
        User user = getCurrentUser();
        user.setDeleted(true);
    }

    @Override
    @Transactional
    public User getCurrentUser() {
        AuthUserDetails currentUserDetails = authenticationFacade.getCurrentUserFromContext();
        if (currentUserDetails != null) {
            return userRepository.findById(currentUserDetails.getUserId()).orElseThrow(() -> new NoUserInContextException(NOT_LOGGED_IN, WrkshtErrors.UNKNOWN));
        } else {
            throw new NoUserInContextException(NOT_LOGGED_IN, WrkshtErrors.UNKNOWN);
        }
    }

    @Override
    @Transactional
    public List<User> getUsers() {
        return userRepository.findAllByIsDeleted(false);
    }

    @Override
    @Transactional
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND, WrkshtErrors.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public User updateUser(String id, UserUpdateDTO user) {
        User u = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND, WrkshtErrors.ENTITY_NOT_FOUND));

        if (u.isDeleted()) {
            throw new InvalidOperationException("Deleted user can not be updated", WrkshtErrors.DELETED_USER);
        }

        User updateUser = fillUpdateUser(u, user);
        return userRepository.save(updateUser);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        User u = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND, WrkshtErrors.ENTITY_NOT_FOUND));

        if (u.getRole() == Role.ADMIN) {
            throw new InvalidOperationException("Admin can not be able to delete another admin", WrkshtErrors.ADMIN_DELETE_ERROR);
        }

        u.setDeleted(true);
    }

    @Override
    @Transactional
    public void makeAdmin(String id) {
        User user = getUserById(id);
        user.setRole(Role.ADMIN);
    }

    @Override
    @Transactional
    public void logout() {
        logoutUser(getCurrentUser());
    }

    @Override
    @Transactional
    public void forceLogout(String id) {
        User user = getUserById(id);
        logoutUser(user);
    }

    private void logoutUser(User user) {
        if (user.getLoggedInStation() != null) {
            user.getLoggedInStation().setLoggedInUser(null);
            user.setLoggedInStation(null);
            userRepository.save(user);
        }
    }

    private User fillUpdateUser(User user, UserUpdateDTO profileDTO) {
        if (profileDTO.getLastName() != null) {
            user.setLastName(profileDTO.getLastName());
        }

        if (profileDTO.getFirstName() != null) {
            user.setFirstName(profileDTO.getFirstName());
        }

        if (profileDTO.getUsername() != null) {
            if (userRepository.findFirstByUsernameAndIdNot(profileDTO.getUsername(), user.getId()).isPresent()) {
                throw new ConflictException(USERNAME_REGISTERED, WrkshtErrors.CONFLICT);
            }
            user.setUsername(profileDTO.getUsername());
        }
        return user;
    }

}
