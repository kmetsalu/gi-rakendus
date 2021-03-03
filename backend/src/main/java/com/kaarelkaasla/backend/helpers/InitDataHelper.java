package com.kaarelkaasla.backend.helpers;

import com.kaarelkaasla.backend.entities.RoleTypes;
import com.kaarelkaasla.backend.entities.Role;
import com.kaarelkaasla.backend.entities.User;
import com.kaarelkaasla.backend.repositories.RoleRepository;
import com.kaarelkaasla.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Initializes a root user and a regular user at application startup
 */

@Component
public class InitDataHelper {

    private static final Logger LOG = LoggerFactory.getLogger(InitDataHelper.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public InitDataHelper(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @EventListener
    public void onStartUp(ApplicationReadyEvent event) {
        User root = new User("root",
                "root@mail.com",
                encoder.encode("admin"),
                true);
        Set<Role> roles = new HashSet<>();
        Role rootRole = roleRepository.findByName(RoleTypes.ROLE_ROOT)
                .orElseThrow(() -> new RuntimeException("Error: The role is not found."));
        roles.add(rootRole);
        root.setRoles(roles);
        userRepository.save(root);

        User user = new User("user",
                "user@mail.com",
                encoder.encode("password"),
                true);
        Set<Role> userRoles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleTypes.ROLE_SEARCH)
                .orElseThrow(() -> new RuntimeException("Error: The role is not found."));
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);

        LOG.info("Data Initialized.");
    }
}
