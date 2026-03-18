package com.simuladorapis.infrastructureadaptadores.web.mapper;

import com.simuladorapis.infrastructureadaptadores.web.dto.LoginResponseDto;
import com.simuladorapis.domain.model.User;

public class LoginMapper {

    public static LoginResponseDto toDTO(User user, String token) {
        return new LoginResponseDto(
                user.getId().value(),
                token
        );
    }
}
