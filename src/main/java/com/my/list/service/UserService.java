package com.my.list.service;

import com.my.list.domain.User;
import com.my.list.domain.UserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserContext.UserContextFactory userContextFactory;

    public UserService(UserMapper userMapper, UserContext.UserContextFactory userContextFactory) {
        this.userMapper = userMapper;
        this.userContextFactory = userContextFactory;
    }

    // ---- CRUD ---- //
    public void add(User object) {
        userMapper.insert(object);
    }
    public User get(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }
    public void update(User object) {
        userMapper.updateByPrimaryKey(object);
    }
    public void remove(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    // ---- Auth ---- //
    public User getByNameAndPass(String name, String pass) {
        return userMapper.selectByNameAndPass(name, pass);
    }

    // ---- Token ---- //
    private final Map<String, UserContext> tokens = new HashMap<>();
    private final Map<Long, UserContext> userContexts = new HashMap<>();
    public String generateToken(String name, String pass) {
        // check password
        User user = getByNameAndPass(name, pass);
        if (user == null) throw new AuthException("Wrong user name or password, name=" + name + ", pass=" + pass);
        // create token and user context
        String token = UUID.randomUUID().toString();
        if (!userContexts.containsKey(user.getId())) userContexts.put(user.getId(), userContextFactory.create(user));
        UserContext userContext = userContexts.get(user.getId());
        // add to tokens map, and return token
        tokens.put(token, userContext);
        return token;
    }
    public void invalidateToken(String token) {
        if (!tokens.containsKey(token)) throw new AuthException("No such token, token=" + token);
        UserContext userContext = tokens.remove(token);
        if (!tokens.containsValue(userContext)) userContexts.remove(userContext.user.getId());
    }
    public UserContext getUserContext(String token) {
        if (!tokens.containsKey(token)) throw new AuthException("No such token, token=" + token);
        return tokens.get(token);
    }

}
