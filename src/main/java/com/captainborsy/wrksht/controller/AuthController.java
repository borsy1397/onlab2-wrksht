package com.captainborsy.wrksht.controller;

import com.captainborsy.wrksht.api.AuthApi;
import com.captainborsy.wrksht.api.model.LoginRequestDTO;
import com.captainborsy.wrksht.api.model.LoginResponseDTO;
import com.captainborsy.wrksht.api.model.PasswordChangeDTO;
import com.captainborsy.wrksht.api.model.RefreshTokenDTO;
import com.captainborsy.wrksht.api.model.TokensDTO;
import com.captainborsy.wrksht.security.AuthoritiesConstants;
import com.captainborsy.wrksht.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticationService authenticationService;

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_SHIFT_LEAD, AuthoritiesConstants.ROLE_WORKER})
    public ResponseEntity<Void> changePassword(@Valid PasswordChangeDTO passwordChangeDTO) {
        authenticationService.changePasswordUser(passwordChangeDTO);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TokensDTO> getAccessToken(@Valid RefreshTokenDTO refreshTokenDTO) {
        TokensDTO trDTO = authenticationService.refreshToken(refreshTokenDTO.getRefreshToken());
        return ResponseEntity.ok(trDTO);
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<List<String>> getRoles() {
        List<String> enums = authenticationService.getRoles();
        return ResponseEntity.ok(enums);
    }

    @Override
    public ResponseEntity<LoginResponseDTO> login(@Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO lrDTO = null;
        try {
            lrDTO = authenticationService.login(loginRequestDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(lrDTO);
    }

    @Override
    @Secured({AuthoritiesConstants.ROLE_ADMIN})
    public ResponseEntity<Void> setPasswordForUser(String userId, @Valid PasswordChangeDTO passwordChangeDTO) {
        authenticationService.setPasswordForUser(userId, passwordChangeDTO);
        return ResponseEntity.ok().build();
    }
}
