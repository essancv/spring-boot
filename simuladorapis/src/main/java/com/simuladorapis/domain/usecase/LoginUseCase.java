package com.simuladorapis.domain.usecase;

import com.simuladorapis.application.usecases.results.LoginResult;

public interface LoginUseCase {

     LoginResult login(String username, String password);
}
