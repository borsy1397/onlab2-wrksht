package com.captainborsy.wrksht.service;


import com.captainborsy.wrksht.api.model.UserRegistrationDTO;
import com.captainborsy.wrksht.api.model.UserUpdateDTO;
import com.captainborsy.wrksht.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserRegistrationDTO newUser);

    User getCurrentUserProfile();

    User updateCurrentUserProfile(UserUpdateDTO profileDTO);

    void deleteCurrentUserProfile();

    User getCurrentUser();

    List<User> getUsers();

    User getUserById(String id);

    User updateUser(String id, UserUpdateDTO updateUser);

    void deleteUser(String id);

    void makeAdmin(String id);

    void logout();

    void forceLogout(String id);
}
