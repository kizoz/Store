package com.store.demo.controller;

import com.store.demo.DTO.AddUserDTO;
import com.store.demo.domain.Role;
import com.store.demo.domain.User;
import com.store.demo.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(path = "/reg")
    public String addUser(@RequestBody AddUserDTO userDTO){
        if(userRepo.findByUsername(userDTO.getUsername())!=null) {
            logger.error("User already exists");
            return "User already exists";
        }

        User user=new User();
        user.setEnabled(true);
        user.setPassword(userDTO.getPassword());
        user.setUsername(userDTO.getUsername());

        if(userDTO.isAdm())
            user.setRole(Collections.singleton(Role.ADMIN));
        else user.setRole(Collections.singleton(Role.USER));

        userRepo.save(user);
        logger.info("New user was saved");
        return "User was saved";
    }





















}
