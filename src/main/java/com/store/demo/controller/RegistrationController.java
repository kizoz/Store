package com.store.demo.controller;

import com.store.demo.domain.Role;
import com.store.demo.domain.User;
import com.store.demo.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(path = "/reg", method = {RequestMethod.POST, RequestMethod.GET})
    public String addUser(@RequestParam String n,
                   @RequestParam String p){
        logger.info("Function started");

        if(userRepo.findByUsername(n)!=null) {
            logger.error("User already exists");
            return "User already exists";
        }
        User user=new User();
        user.setPassword(p);
        user.setEnabled(true);
        user.setUsername(n);
        user.setRole(Collections.singleton(Role.USER));
        userRepo.save(user);
        logger.info("New user was saved");
        return "User was saved";
    }

    @RequestMapping(path = "/regadmin", method = {RequestMethod.GET,RequestMethod.POST})
    public String addAdmin(@RequestParam String n,
                    @RequestParam String p){
        if(userRepo.findByUsername(n)!=null) {
            logger.error("Admin already exists");
            return "Admin already exists";
        }
        User user=new User();
        user.setPassword(p);
        user.setEnabled(true);
        user.setUsername(n);
        user.setRole(Collections.singleton(Role.ADMIN));
        userRepo.save(user);
        logger.info("New admin was saved");
        return "Admin was saved";
    }





















}
