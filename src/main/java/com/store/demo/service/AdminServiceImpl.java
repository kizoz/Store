package com.store.demo.service;

import com.store.demo.domain.User;
import com.store.demo.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    private final UserRepo userRepo;
    @Autowired
    public AdminServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void banUser(String username) {
        User user=userRepo.findByUsername(username);
        if(user!=null) {
            logger.debug("User "+username+" was banned");
            user.setEnabled(false);
            userRepo.save(user);
        }
        else logger.debug("User does not exist");
    }

    @Override
    public void unBanUser(String username) {
        User user=userRepo.findByUsername(username);
        if(user!=null) {
            logger.debug("User "+username+" was unbanned");
            user.setEnabled(true);
            userRepo.save(user);
        }
        else logger.debug("User does not exist");
    }
}
