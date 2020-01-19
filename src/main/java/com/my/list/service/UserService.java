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

    public UserService(UserMapper userMapper, UserContext.UserContextFactory userContextFactory) {
        this.userMapper = userMapper;
        this.userContextFactory = userContextFactory;
    }

    // ---- CRUD ---- //
    public void add(User user) {
        if (user == null) throw new DataException("Input user is null");
        if (user.getId() != null) throw new DataException("Id of input user has already set.");
        userMapper.insert(user);
    }
    public User get(Long id) {
        if (id == null) throw new DataException("Input id is null");
        return userMapper.selectByPrimaryKey(id);
    }
    public void update(User user) {
        if (user == null) throw new DataException("Input user is null");
        if (user.getId() == null) throw new DataException("Id of input user is not set.");
        userMapper.updateByPrimaryKey(user);
    }
    public void remove(Long id) {
        if (id == null) throw new DataException("Input id is null");
        userMapper.deleteByPrimaryKey(id);
    }

    // ---- Auth ---- //
    public User getByNameAndPass(String name, String pass, boolean safe) {
        if (name == null) throw new DataException("Input name is null");
        if (pass == null) throw new DataException("Input password is null");
        return safe ?
            userMapper.selectByNameAndPass(name, pass) :
            userMapper.selectByNameAndPassUnsafe(name, pass);
    }

    // ---- Token ---- //
    private final Map<String, UserContext> tokens = new HashMap<>();
    private final Map<Long, UserContext> userContexts = new HashMap<>();
    private final UserContext.UserContextFactory userContextFactory;

    public String generateToken(String name, String pass, boolean safe) {
        if (name == null) throw new DataException("Input name is null");
        if (pass == null) throw new DataException("Input password is null");
        // check password
        User user = getByNameAndPass(name, pass, safe);
        if (user == null) throw new AuthException("Wrong user name or password, name=" + name + ", pass=" + pass + ", safe=" + safe);
        // create token and user context
        String token = UUID.randomUUID().toString();
        if (!userContexts.containsKey(user.getId())) userContexts.put(user.getId(), userContextFactory.create(user));
        UserContext userContext = userContexts.get(user.getId());
        // add to tokens map, and return token
        tokens.put(token, userContext);
        return token;
    }
    public void invalidateToken(String token) {
        if (token == null) throw new DataException("Input token is null");
        if (!tokens.containsKey(token)) throw new AuthException("No such token, token=" + token);
        UserContext userContext = tokens.remove(token);
        if (!tokens.containsValue(userContext)) userContexts.remove(userContext.user.getId());
    }
    public UserContext getUserContext(String token) {
        if (token == null) throw new DataException("Input token is null");
        if (!tokens.containsKey(token)) throw new AuthException("No such token, token=" + token);
        return tokens.get(token);
    }

}
