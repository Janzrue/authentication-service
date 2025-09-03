package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.auth.AuthConstants;
import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.usecase.auth.AuthUseCase.RolesResolver;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class RolesResolverAdapter implements RolesResolver {
    @Override
    public List<String> resolve(User user) {
        if (user.getIdRole() == null) return Collections.emptyList();
        long rid = user.getIdRole().longValue();
        if (rid == 1L) return List.of(AuthConstants.ROLE_ADMIN);
        if (rid == 2L) return List.of(AuthConstants.ROLE_ASESOR);
        if (rid == 3L) return List.of(AuthConstants.ROLE_CLIENTE);
        return Collections.emptyList();
    }
}
