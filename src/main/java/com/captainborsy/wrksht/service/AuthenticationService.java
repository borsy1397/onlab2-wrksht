package com.captainborsy.wrksht.service;


import com.captainborsy.wrksht.api.model.LoginRequestDTO;
import com.captainborsy.wrksht.api.model.LoginResponseDTO;
import com.captainborsy.wrksht.api.model.PasswordChangeDTO;
import com.captainborsy.wrksht.api.model.TokensDTO;

import java.io.IOException;
import java.util.List;

public interface AuthenticationService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTODTO) throws IOException;

    void changePasswordUser(PasswordChangeDTO passwordChangeDTO);

    TokensDTO refreshToken(String refreshToken);

    List<String> getRoles();

    void setPasswordForUser(String userId, PasswordChangeDTO passwordChangeDTO);
}
