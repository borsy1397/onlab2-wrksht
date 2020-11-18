package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.api.model.LoginRequestDTO;
import com.captainborsy.wrksht.api.model.LoginResponseDTO;
import com.captainborsy.wrksht.api.model.PasswordChangeDTO;
import com.captainborsy.wrksht.api.model.TokensDTO;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.CredentialException;
import com.captainborsy.wrksht.errorhandling.exception.EntityNotFoundException;
import com.captainborsy.wrksht.errorhandling.exception.InvalidTokenException;
import com.captainborsy.wrksht.mapper.UserMapper;
import com.captainborsy.wrksht.model.Role;
import com.captainborsy.wrksht.model.User;
import com.captainborsy.wrksht.repository.UserRepository;
import com.captainborsy.wrksht.service.AuthenticationService;
import com.captainborsy.wrksht.service.TokenService;
import com.captainborsy.wrksht.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String INCORRECT = "Username or password incorrect";
    private static final String INVALID_PASSWORD = "Invalid current password during password change";
    private static final String ACCESS_DENIED = "Access denied";
    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        if (!StringUtils.hasLength(loginRequestDTO.getUsername()) || !StringUtils.hasLength(loginRequestDTO.getPassword())) {
            throw new CredentialException(INCORRECT, WrkshtErrors.UNAUTHORIZED);
        }

        Optional<User> optionalUser = userRepository.findByUsername(loginRequestDTO.getUsername());

        if (optionalUser.isEmpty() || optionalUser.get().getPassword() == null) {
            throw new CredentialException(INCORRECT, WrkshtErrors.UNAUTHORIZED);
        }

        User user = optionalUser.get();

        if (passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            // TODO station bejelentkezés
            return createLoginResponseDTO(user);
        } else {
            throw new CredentialException(INVALID_PASSWORD, WrkshtErrors.INVALID_PASSWORD);
        }
    }

    @Override
    @Transactional
    public void changePasswordUser(PasswordChangeDTO passwordChangeDTO) {
        User currentUser = userService.getCurrentUser();

        if (passwordChangeDTO.getCurrentPassword() == null) {
            throw new CredentialException(INVALID_PASSWORD, WrkshtErrors.INVALID_PASSWORD);
        }

        if (passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
            userRepository.save(currentUser);
        } else {
            throw new CredentialException(INVALID_PASSWORD, WrkshtErrors.INVALID_PASSWORD);
        }

    }

    @Override
    public List<String> getRoles() {
        return Stream.of(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void setPasswordForUser(String userId, PasswordChangeDTO passwordChangeDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND, WrkshtErrors.ENTITY_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public TokensDTO refreshToken(String rt) {
        String username = tokenService.validateRefreshToken(rt);

        Optional<User> u = userRepository.findByUsername(username);
        if (u.isEmpty()) {
            throw new InvalidTokenException("Access denied", WrkshtErrors.ACCESS_DENIED);
        }

        User user = u.get();

        String accessToken = tokenService.generateAccessToken(user.getUsername(), user.getId(), user.getRole());
        String refreshToken = tokenService.generateRefreshToken(user.getUsername(), user.getId(), user.getRole());

        return TokensDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }


    private LoginResponseDTO createLoginResponseDTO(User user) {
        String accessToken = tokenService.generateAccessToken(user.getUsername(), user.getId(), user.getRole());
        String refreshToken = tokenService.generateRefreshToken(user.getUsername(), user.getId(), user.getRole());

        return LoginResponseDTO.builder()
                .tokens(TokensDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build())
                .user(UserMapper.mapUserToUserDTO(user))
                .build();

    }

}
