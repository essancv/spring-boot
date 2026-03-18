package com.simuladorapis.infrastructureadaptadores.web;

import com.simuladorapis.domain.usecase.LoginUseCase;
import com.simuladorapis.infrastructureadaptadores.web.dto.LoginRequestDto;
import com.simuladorapis.infrastructureadaptadores.web.dto.LoginResponseDto;
import com.simuladorapis.infrastructureadaptadores.web.mapper.LoginMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginUseCase loginUseCase;

    public LoginController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        try {
//            String token = loginUseCase.login(request.getUsername(), request.getPassword());
//            return ResponseEntity.ok(new LoginResponseDto(token));
            var result = loginUseCase.login(request.getUsername(), request.getPassword());
            var dto = LoginMapper.toDTO(result.user(), result.token());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
