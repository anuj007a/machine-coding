package service.impl;

import model.User;
import service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserServiceImpl implements UserService {
    private List<User> userList;

    public UserServiceImpl() {
        userList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public User getUserByUserId(String userId){
        for(User user : userList){
            if(Objects.equals(user.getUserId(), userId)){
                return user;
            }
        }
        return null;
    }

    public User signupUser(String name){
        User user = new User(name);
        userList.add(user);
        return user;
    }

}
