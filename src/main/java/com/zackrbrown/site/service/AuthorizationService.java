package com.zackrbrown.site.service;

import com.zackrbrown.site.model.Permissions;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class AuthorizationService {

    public boolean isAuthorized(Principal principal, Permissions permission) {
        return "zrbrown".equals(principal.getName());
    }
}
