package com.itgate.ProShift.service;

import com.itgate.ProShift.entity.ERole;
import com.itgate.ProShift.entity.Role;
import com.itgate.ProShift.entity.User;
import com.itgate.ProShift.repository.RoleRepository;
import com.itgate.ProShift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<String> requiredRoles = Arrays.asList("ROLE_EMPLOYEE", "ROLE_COORDINATEUR", "ROLE_CHEF", "ROLE_RESPONSABLE");
        for (String roleName : requiredRoles) {
            if (roleRepository.findByName(ERole.valueOf(roleName)).orElse(null)==null) {
                Role role = new Role(ERole.valueOf(roleName));
                roleRepository.save(role);
            }
        }
        String adminUsername = "admin";
        if (!userRepository.existsByUsername(adminUsername)) {
            Role adminRole = roleRepository.findByName(ERole.valueOf("ROLE_RESPONSABLE")).get();
            Set<Role> roles =new HashSet<>();
            roles.add(adminRole);
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode("admin")); // Replace with your desired admin password
            adminUser.setRoles(roles);
            adminUser.setEmail("admin@admin.com");
            adminUser.setApproved(true);
            userRepository.save(adminUser);
        }
    }
}

