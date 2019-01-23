package com.my.list.service;

import com.my.list.data.Token;
import com.my.list.data.User;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class TokenService {

    private Map<String, Token> tokens = new HashMap<>();

    public Token createToken(@NotNull User user) {
        String token = UUID.randomUUID().toString();
        Token tokenEntity = new Token(token, user);
        tokens.put(token, tokenEntity);
        return tokenEntity;
    }

    public boolean checkToken(Token tokenEntity) {
        if (tokenEntity == null) {
            return false;
        }
        Token token = tokens.get(tokenEntity.getToken());
        if (token != null) {
            tokenEntity.setUser(token.getUser());
            return true;
        } else {
            return false;
        }
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }

    public void removeToken(User user) {
        tokens.entrySet().removeIf(entry -> Objects.requireNonNull(entry.getValue().getUser()).getId() == user.getId());
    }
}
