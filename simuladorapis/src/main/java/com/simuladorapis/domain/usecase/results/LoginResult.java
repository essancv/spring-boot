package com.simuladorapis.application.usecases.results;

import com.simuladorapis.domain.model.User;

public record LoginResult(
        User user,
        String token
) {}
