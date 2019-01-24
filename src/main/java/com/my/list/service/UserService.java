package com.my.list.service;

import com.my.list.data.User;
import com.my.list.data.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
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

    @NotNull
    public User getUser(@NotNull String name) throws DataException {
        User user = userRepository.getByName(name);
        if (user != null) {
            return user;
        } else {
            logger.info("getUser: User(" + name + ") Not Exist");
            throw new DataException("User(" + name + ") Not Exist", ErrorType.NOT_FOUND);
        }
    }

    @NotNull
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(@NotNull User user) throws DataException {
        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())) {
            logger.info("addUser: Name Or Password Is Null");
            throw new DataException("Name Or Password Is Empty", ErrorType.BAD_REQUEST);
        }
        if (!userRepository.existsByName(user.getName())) {
            try {
                userRepository.save(user);
            } catch (Exception e) {
                logger.info("addUser: An Error Occurred: " + e.getMessage());
                throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
            }
        } else {
            logger.info("addUser: User Already Exist");
            throw new DataException("User Already Exist", ErrorType.ALREADY_EXIST);
        }
    }

    public boolean checkUser(String name, String password) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            logger.info("checkUser: Name Or Password Is Null");
            return false;
        }
        try {
            User user = userRepository.getByName(name);
            return encrypt(password).equals(user.getPassword());
        } catch (Exception e) {
            logger.info("checkUser: An Error Occurred: " + e.getMessage());
            return false;
        }
    }

    String encrypt(String message) {
        return new BigInteger(1, msgDigest.digest(message.getBytes())).toString(16);
    }
}
