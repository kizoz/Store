package com.store.demo.controller;

import com.store.demo.domain.Role;
import com.store.demo.domain.User;
import com.store.demo.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class RegistrationController {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    private final UserRepo userRepo;

    @Autowired
    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping(path = "/reg/{property}")
    public String addUser(@RequestBody User user, @PathVariable String property){
        if(userRepo.findByUsername(user.getUsername())!=null) {
            logger.error("User already exists");
            return "User already exists";
        }
        switch (property){
            case "u":
                user.setRole(Collections.singleton(Role.USER));
                break;
            case "a":
                user.setRole(Collections.singleton(Role.ADMIN));
                break;
            default:
                throw new IllegalArgumentException("Invalid property, type in 'a' if you want to register admin, or 'u' if you want to register user");
        }
        userRepo.save(user);
        logger.info("New user was saved");
        return "User was saved";
    }





















}
