package com.my.list.service;

import com.my.list.data.User;
import com.my.list.data.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final MessageDigest msgDigest;

    @Autowired
    public UserService(UserRepository userRepository) throws NoSuchAlgorithmException {
        this.userRepository = userRepository;
        this.msgDigest = MessageDigest.getInstance("SHA");
    }

    public boolean checkUser(String name, String password) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            logger.info("checkUser: Name Or Password Is Null!");
            return false;
        }
        try {
            User user = userRepository.getByName(name);
            return encrypt(password).equals(user.getPassword());
        } catch (Exception e) {
            logger.info("checkUser: An Error Occur: " + e.getMessage());
            return false;
        }
    }

    public int addUser(String name, String password) throws Exception {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            logger.info("addUser: Name Or Password Is Null!");
            throw new Exception("Name Or Password Is Empty!");
        }
        if (!userRepository.existsByName(name)) {
            User user = new User();
            user.setName(name);
            user.setPassword(encrypt(password));
            try {
                userRepository.save(user);
                return user.getId();
            } catch (Exception e) {
                logger.warn("addUser: An Error Occur: " + e.getMessage());
                throw new Exception(e);
            }
        } else {
            logger.warn("addUser: User Already Exist!");
            throw new Exception("User Already Exist");
        }
    }

    public User getUser(String name) {
        return userRepository.getByName(name);
    }

    public Iterable<User> listUsers() {
        return userRepository.findAll();
    }

    String encrypt(String message) {
        return new BigInteger(1, msgDigest.digest(message.getBytes())).toString(16);
    }
}
