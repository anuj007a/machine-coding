package service;

import model.User;

public interface UserService {
    public User getUserByUserId(String userId);
    public User signupUser(String name);
}
