package service.impl;

import service.SessionService;

import java.util.Objects;

public class SessionServiceImpl implements SessionService {
    String currUserId;

    public SessionServiceImpl() {
        currUserId = "";
    }

    public void login(String userId) throws Exception {
        if(!Objects.equals(currUserId, "")){
            throw new Exception("One user has already being logged in");
        }
        currUserId = userId;
    }

    public void logout(String userId) throws Exception{
        if(Objects.equals(currUserId, "")){
            throw new Exception("No one has been logged in");
        }
        currUserId = "";
    }
}
