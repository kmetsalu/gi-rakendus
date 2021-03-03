package com.kaarelkaasla.backend.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller class for handling the requests that test whether an user is authorized to access a route.
 * Methods return true if an user has the appropriate role.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/access")
public class AccessController {

    @GetMapping("/search")
    @PreAuthorize("hasRole('SEARCH') or hasRole('ROOT')")
    public Boolean searchAccess() {
        return true;
    }

    @GetMapping("/upload")
    @PreAuthorize("hasRole('UPLOAD') or hasRole('ROOT')")
    public Boolean uploadAccess() {
        return true;
    }

    @GetMapping("/management")
    @PreAuthorize("hasRole('MANAGEMENT') or hasRole('ROOT')")
    public Boolean managementAccess() {
        return true;
    }
}
